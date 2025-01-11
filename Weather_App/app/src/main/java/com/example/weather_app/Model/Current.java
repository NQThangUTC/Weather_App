package com.example.weather_app.Model;

public class Current {
    private String last_updated;
    private double temp_c;
    private Condition condition;
    private double wind_kph;
    private int humidity;
    private double precip_mm;

    public Current(String last_updated, double temp_c, Condition condition, double wind_kph, int humidity, double precip_mm) {
        this.last_updated = last_updated;
        this.temp_c = temp_c;
        this.condition = condition;
        this.wind_kph = wind_kph;
        this.humidity = humidity;
        this.precip_mm = precip_mm;
    }

    public double getWind_kph() {
        return wind_kph;
    }

    public void setWind_kph(double wind_kph) {
        this.wind_kph = wind_kph;
    }

    public double getPrecip_mm() {
        return precip_mm;
    }

    public void setPrecip_mm(double precip_mm) {
        this.precip_mm = precip_mm;
    }

    public String getLast_updated() {
        return last_updated;
    }

    public void setLast_updated(String last_updated) {
        this.last_updated = last_updated;
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

    public double getWind_kmp() {
        return wind_kph;
    }

    public void setWind_kmp(double wind_kmp) {
        this.wind_kph = wind_kmp;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }
}
