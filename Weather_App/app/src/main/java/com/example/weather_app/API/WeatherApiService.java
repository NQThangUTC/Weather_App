package com.example.weather_app.API;

import com.example.weather_app.Model.WeatherResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherApiService {
    @GET("current.json")
    Call<WeatherResponse> getWeather(
            @Query("key") String apiKey,
            @Query("q") String cityName
    );
    @GET("forecast.json")
    Call<WeatherResponse> getWeatherHour(
            @Query("key") String apiKey,
            @Query("q") String cityName,
            @Query("days") int days
    );
}
