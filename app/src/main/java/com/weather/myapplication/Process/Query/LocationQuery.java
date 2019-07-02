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

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationQuery implements DataChangeListener {

    private FusedLocationProviderClient fusedLocationClient;
    Activity activity;

    public LocationQuery(Activity activity) {
        this.activity = activity;
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
                                        String city = getCurrentLocation(location.getLatitude(), location.getLongitude());
                                    } catch (
                                            Exception e) {
                                        e.printStackTrace();
                                        activity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(activity, "Not found", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                }
                            });

                }
            }
        };
        thread.start();


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
        }

        return cityName;
    }

    @Override
    public void onCurrentResponseReceived(CurrentResponse currentResponse) {
        
    }

    @Override
    public void onForeCastResponseReceived(ForeCastResponse foreCastResponse) {

    }

    @Override
    public void onError(String message) {

    }
}
