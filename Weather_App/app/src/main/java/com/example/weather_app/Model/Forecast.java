package com.example.weather_app.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Forecast {
    @SerializedName("forecastday")

    private List<ForecastDay> forecastDay;

    public List<ForecastDay> getForecastDay() {
        return forecastDay;
    }

    public void setForecastDay(List<ForecastDay> forecastDay) {
        this.forecastDay = forecastDay;
    }
}
