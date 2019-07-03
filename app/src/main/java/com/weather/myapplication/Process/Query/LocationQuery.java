package com.weather.myapplication.Process.Query;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.weather.myapplication.MainActivity;
import com.weather.myapplication.Process.API.Model.CurrentResponse;
import com.weather.myapplication.Process.API.Model.ForeCastResponse;
import com.weather.myapplication.Process.DataChangeListener;
import com.weather.myapplication.Process.ProcessListener;
import com.weather.myapplication.Utils.NetworkConfig;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationQuery implements DataChangeListener {

    private FusedLocationProviderClient fusedLocationClient;
    Activity activity;
    String currentCity = "";
    String lat = "";
    String lng = "";
    ProcessListener processListener;

    public LocationQuery(Activity activity, ProcessListener processListener) {
        this.activity = activity;
        this.processListener = processListener;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
    }

    public void getCurrentLatLong() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                super.run();
                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M &&
                        activity.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    activity.requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
                } else {
                    fusedLocationClient.getLastLocation()
                            .addOnSuccessListener(activity, new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    try {
                                        currentCity = getCurrentLocation(location.getLatitude(), location.getLongitude());
                                        lat = "" + location.getLatitude();
                                        lng = "" + location.getLongitude();
                                        getCurrentData();
                                    } catch (
                                            Exception e) {
                                        e.printStackTrace();
                                        processListener.showError(true);
                                    }
                                }
                            });

                }
            }
        };
        thread.start();
    }

    public void showInternetMessage() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(activity, "No internet connection", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getCurrentData() {

        if (!new NetworkConfig().isInternetAvailable(activity)) {
            processListener.showError(true);
            showInternetMessage();
            return;
        }
        APIQuery apiQuery = new APIQuery();
        if (!currentCity.isEmpty())
            apiQuery.getCurrentData(this, activity, currentCity);
        else if (!lat.isEmpty() && !lng.isEmpty())
            apiQuery.getCurrentData(this, activity, lat + "," + lng);
        else
            processListener.showError(true);
        getForeCastData();
    }

    public void getForeCastData() {

        if (!new NetworkConfig().isInternetAvailable(activity)) {
            processListener.showError(true);
            showInternetMessage();
            return;
        }
        APIQuery apiQuery = new APIQuery();
        if (!currentCity.isEmpty())
            apiQuery.getForeCastData(this, activity, currentCity);
        else if (!lat.isEmpty() && !lng.isEmpty())
            apiQuery.getForeCastData(this, activity, lat + "," + lng);
        else
            processListener.showError(true);
    }

    private String getCurrentLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(activity, Locale.getDefault());
        List<Address> addresses;
        String cityName = "";

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 10);
            if (addresses.size() > 0) {
                for (Address address : addresses) {
                    if (address.getLocality() != null && address.getLocality().length() > 0) {
                        cityName = address.getLocality();
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            processListener.showError(true);
        }

        return cityName;
    }

    @Override
    public void onCurrentResponseReceived(CurrentResponse currentResponse) {
        if (currentResponse == null) {
            processListener.showError(true);
            return;
        }
        processListener.showCurrentWeather(currentResponse.getCurrent().getTemp_c(), currentResponse.getLocation().getName());
        processListener.showError(false);
    }

    @Override
    public void onForeCastResponseReceived(ForeCastResponse foreCastResponse) {
        if (foreCastResponse == null) {
            processListener.showError(true);
            return;
        }
        processListener.showForeCastData(foreCastResponse);
        processListener.showError(false);
    }

    @Override
    public void onError(String message) {
        processListener.showError(true);
    }
}
