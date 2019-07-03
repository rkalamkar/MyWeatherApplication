package com.weather.myapplication.Process.API.Model;

public class ForeCastResponse {
    Location location;
    Current current;
    ForCast forecast;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Current getCurrent() {
        return current;
    }

    public void setCurrent(Current current) {
        this.current = current;
    }

    public ForCast getForeCast() {
        return forecast;
    }

    public void setForeCast(ForCast foreCast) {
        this.forecast = foreCast;
    }
}
