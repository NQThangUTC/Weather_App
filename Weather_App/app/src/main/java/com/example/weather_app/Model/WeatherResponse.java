package com.example.weather_app.Model;

import com.google.gson.annotations.SerializedName;

public class WeatherResponse {
    @SerializedName("location")

    private Location location;
    @SerializedName("current")

    private Current current;
    @SerializedName("forecast")

    private Forecast forecast;

    public WeatherResponse(Location location, Current current, Forecast forecast) {
        this.location = location;
        this.current = current;
        this.forecast = forecast;
    }

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

    public Forecast getForecast() {
        return forecast;
    }

    public void setForecast(Forecast forecast) {
        this.forecast = forecast;
    }
}

