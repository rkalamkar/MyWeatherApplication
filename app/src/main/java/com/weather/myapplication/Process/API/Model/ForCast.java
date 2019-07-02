package com.weather.myapplication.Process.API.Model;

import java.util.ArrayList;

public class ForCast {
    ArrayList<forecastday> forecastdays;

    public ArrayList<forecastday> getForecastdays() {
        return forecastdays;
    }

    public void setForecastdays(ArrayList<forecastday> forecastdays) {
        this.forecastdays = forecastdays;
    }
}
