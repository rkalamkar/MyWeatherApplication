package com.weather.myapplication.Process.API.Model;

public class forecastday {
    String date;
    String date_epoch;
    Astro astro;
    Day day;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate_epoch() {
        return date_epoch;
    }

    public void setDate_epoch(String date_epoch) {
        this.date_epoch = date_epoch;
    }

    public Astro getAstro() {
        return astro;
    }

    public void setAstro(Astro astro) {
        this.astro = astro;
    }

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }
}
