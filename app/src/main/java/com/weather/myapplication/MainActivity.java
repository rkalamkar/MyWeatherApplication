package com.weather.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.weather.myapplication.Process.API.Model.CurrentResponse;
import com.weather.myapplication.Process.API.Model.ForeCastResponse;
import com.weather.myapplication.Process.DataChangeListener;
import com.weather.myapplication.Process.ProcessListener;
import com.weather.myapplication.Process.Query.LocationQuery;
import com.weather.myapplication.View.WeatherAdapter;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements ProcessListener {

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;

    @BindView(R.id.buttonRetry)
    Button mRetryButton;

    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    @BindView(R.id.textViewCityName)
    TextView mCityName;

    @BindView(R.id.textViewTempreture)
    TextView mCityTemperature;

    @BindView(R.id.errorMessage)
    TextView mErrorMessage;

    @BindView(R.id.mainLayout)
    LinearLayout bottomLinearLayout;

    @BindView(R.id.errorLayout)
    View eView;

    LocationQuery locationQuery;
    BottomSheetBehavior bottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        showError(false);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomLinearLayout);
        stopScroll();
        callData();
    }

    public void stopScroll() {
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState > BottomSheetBehavior.STATE_DRAGGING)
                    bottomSheet.post(new Runnable() {
                        @Override
                        public void run() {
                            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        }
                    });
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
    }

    public void bottomSheetCall(boolean isExpand) {
        if (isExpand)
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        else
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1000: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callData();
                } else {
                    showError(true);
                    Toast.makeText(this, "Permission not granted", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

    @Override
    public void showError(boolean isShow) {
        if (isShow) {
            eView.setVisibility(View.VISIBLE);
            mRetryButton.setVisibility(View.VISIBLE);
            mErrorMessage.setVisibility(View.VISIBLE);
        } else {
            eView.setVisibility(View.GONE);
            mRetryButton.setVisibility(View.GONE);
            mErrorMessage.setVisibility(View.GONE);
        }
        mProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showCurrentWeather(final String temperature, final String cityName) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mCityName.setText(cityName);
                mCityName.setVisibility(View.VISIBLE);
                mCityTemperature.setText(temperature + "\u00B0");
                mCityTemperature.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void showForeCastData(final ForeCastResponse foreCastResponse) {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                WeatherAdapter weatherAdapter = new WeatherAdapter(MainActivity.this, foreCastResponse.getForeCast().getForecastdays());
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
                mRecyclerView.setLayoutManager(linearLayoutManager);
                mRecyclerView.setAdapter(weatherAdapter);
                bottomSheetCall(true);
            }
        });
    }

    public void callData() {
        mProgressBar.setVisibility(View.VISIBLE);
        if (locationQuery != null)
            locationQuery.getCurrentLatLong();
        else {
            locationQuery = new LocationQuery(this, this);
            locationQuery.getCurrentLatLong();
        }
    }

    @OnClick(R.id.buttonRetry)
    public void onClick(View v) {
        if (v.getId() == R.id.buttonRetry) {
            callData();
        }
    }
}
