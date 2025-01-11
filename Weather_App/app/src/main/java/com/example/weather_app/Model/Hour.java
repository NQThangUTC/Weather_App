package com.example.weather_app.Model;

public class Hour {
    private String time;
    private double temp_c;
    private int is_day;
    private Condition condition;

    public Hour(String time, double temp_c, Condition condition) {
        this.time = time;
        this.temp_c = temp_c;
        this.condition = condition;
    }

    public int getIs_day() {
        return is_day;
    }

    public void setIs_day(int is_day) {
        this.is_day = is_day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getTemp_c() {
        return temp_c;
    }

    public void setTemp_c(double temp_c) {
        this.temp_c = temp_c;
    }

    public Condition getCondition() {
        return condition;
    }

    public void setCondition(Condition condition) {
        this.condition = condition;
    }
}
