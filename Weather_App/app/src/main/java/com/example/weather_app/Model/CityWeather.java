package com.example.weather_app.Model;

import com.google.gson.annotations.SerializedName;

public class CityWeather {
    private String cityName;
    private Condition condition;
    private double minTemp;
    private double maxTemp;

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public void setMinTemp(double minTemp) {
        this.minTemp = minTemp;
    }

    public void setMaxTemp(double maxTemp) {
        this.maxTemp = maxTemp;
    }

    public String getCityName() {
        return cityName;
    }


    public double getMinTemp() {
        return minTemp;
    }

    public double getMaxTemp() {
        return maxTemp;
    }
}
