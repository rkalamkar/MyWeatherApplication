package com.weather.myapplication.Process.API;

import com.weather.myapplication.Process.API.Model.CurrentResponse;
import com.weather.myapplication.Process.API.Model.ForeCastResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface Query {

    @GET("{FETCH_CURRENT}")
    Call<CurrentResponse> getCurrentData(@Path(value = "FETCH_CURRENT", encoded = true) String path, @QueryMap Map<String, String> queryMap);


    @GET("{FETCH_FORECAST}")
    Call<ForeCastResponse> getForeCastData(@Path(value = "FETCH_FORECAST", encoded = true) String path, @QueryMap Map<String, String> queryMap);
}
