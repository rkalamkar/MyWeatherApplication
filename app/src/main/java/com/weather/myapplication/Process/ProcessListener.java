package com.weather.myapplication.Process;

import com.weather.myapplication.Process.API.Model.ForeCastResponse;

public interface ProcessListener {
    void showError(boolean isShow);

    void showCurrentWeather(String temperature, String cityName);

    void showForeCastData(ForeCastResponse foreCastResponse);
}
