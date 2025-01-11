package com.example.weather_app.Model;

public class Location {
    private String name;
    private String region;
    private String country;

    public Location(String name, String region, String country) {
        this.name = name;
        this.region = region;
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }
}
