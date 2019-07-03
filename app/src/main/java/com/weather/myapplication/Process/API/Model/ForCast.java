package com.weather.myapplication.Process.API.Model;

import java.util.ArrayList;

public class ForCast {
    ArrayList<forecastday> forecastday;

    public ArrayList<forecastday> getForecastdays() {
        return forecastday;
    }

    public void setForecastdays(ArrayList<forecastday> forecastdays) {
        this.forecastday = forecastdays;
    }
}
