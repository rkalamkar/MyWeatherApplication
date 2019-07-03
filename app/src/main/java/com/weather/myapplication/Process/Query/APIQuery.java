package com.weather.myapplication.Process.Query;

import android.app.Activity;

import com.weather.myapplication.Process.API.APIClient;
import com.weather.myapplication.Process.API.Model.CurrentResponse;
import com.weather.myapplication.Process.API.Model.ForeCastResponse;
import com.weather.myapplication.Process.API.Query;
import com.weather.myapplication.Process.DataChangeListener;
import com.weather.myapplication.R;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class APIQuery {
    public void getCurrentData(final DataChangeListener dataChangeListener, Activity activity, String city) {
        URL url = null;

        try {
            url = new URL("https://api.apixu.com/v1/current.json");


            String baseUrl = url.getProtocol() + "://" + url.getHost();
            String apiName = url.getPath();

            HashMap<String, String> map = new HashMap<>();
            map.put("Key", activity.getResources().getString(R.string.api_id));
            map.put("q", city);

            Query query = APIClient.getClient(baseUrl).create(Query.class);
            Call<CurrentResponse> currentResponseCall = query.getCurrentData(apiName, map);
            currentResponseCall.enqueue(new Callback<CurrentResponse>() {
                @Override
                public void onResponse(Call<CurrentResponse> call, Response<CurrentResponse> response) {
                    dataChangeListener.onCurrentResponseReceived(response.body());
                }

                @Override
                public void onFailure(Call<CurrentResponse> call, Throwable t) {
                    dataChangeListener.onError(t.getMessage());
                }
            });

        } catch (MalformedURLException e) {
            e.printStackTrace();
            dataChangeListener.onError(e.getMessage());
        }
    }

    public void getForeCastData(final DataChangeListener dataChangeListener, Activity activity, String city) {
        URL url = null;
        try {
            url = new URL("https://api.apixu.com/v1/forecast.json?Key=a8918b6f226548d1b7a45149190207&q=Jalna&days=4");
            String baseUrl = url.getProtocol() + "://" + url.getHost();
            String apiName = url.getPath();

            HashMap<String, String> map = new HashMap<>();
            map.put("Key", activity.getResources().getString(R.string.api_id));
            map.put("q", city);
            map.put("days", "4");

            Query query = APIClient.getClient(baseUrl).create(Query.class);
            Call<ForeCastResponse> foreCastResponseCall = query.getForeCastData(apiName, map);

            foreCastResponseCall.enqueue(new Callback<ForeCastResponse>() {
                @Override
                public void onResponse(Call<ForeCastResponse> call, Response<ForeCastResponse> response) {
                    dataChangeListener.onForeCastResponseReceived(response.body());
                }

                @Override
                public void onFailure(Call<ForeCastResponse> call, Throwable t) {
                    dataChangeListener.onError(t.getMessage());
                }
            });
        } catch (MalformedURLException e) {
            e.printStackTrace();
            dataChangeListener.onError(e.getMessage());
        }
    }
}
