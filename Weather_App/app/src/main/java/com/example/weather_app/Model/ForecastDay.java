package com.example.weather_app.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ForecastDay {
    @SerializedName("day")
    private Day day;
    @SerializedName("hour")
    private List<Hour> hour;

    public Day getDay() {
        return day;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    public List<Hour> getHour() {
        return hour;
    }

    public void setHour(List<Hour> hour) {
        this.hour = hour;
    }
}
