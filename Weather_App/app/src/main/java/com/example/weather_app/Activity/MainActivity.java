package com.example.weather_app.Activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.weather_app.API.RetrofitClient;
import com.example.weather_app.API.WeatherApiService;
import com.example.weather_app.Adapter.HourAdapter;
import com.example.weather_app.Model.Hour;
import com.example.weather_app.Model.WeatherResponse;
import com.example.weather_app.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private EditText editCity;
    private Button btnGetWeather;
    private TextView tempText, humidityText,windText, rainText;
    private ImageView icon;
    private RecyclerView recyclerView;
    private HourAdapter hourAdapter;
    private final String apiKey = "db5c9522a88247b79f672526251001";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectXML();

        btnGetWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cityName = editCity.getText().toString().trim();
                if (cityName.isEmpty()){
                    Toast.makeText(MainActivity.this, "City not empty", Toast.LENGTH_SHORT).show();
                }else {
                    fetchWeather(cityName);
                    fetchWeatherHour(cityName);
                }
            }
        });
    }

    private void fetchWeather(String cityName) {
        WeatherApiService apiService = RetrofitClient.getRetrofitInstance().create(WeatherApiService.class);
        Call<WeatherResponse> call = apiService.getWeather(apiKey, cityName);
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if(response.isSuccessful() && response.body() != null){
                    WeatherResponse weather = response.body();

                    double temp = weather.getCurrent().getTemp_c();
                    tempText.setText(String.format("%.1f°C",temp));
                    humidityText.setText(String.format("%d%%",weather.getCurrent().getHumidity()));
                    windText.setText(String.format("%.1f Km/h",weather.getCurrent().getWind_kmp()));
                    rainText.setText(String.format("%.1f%%",weather.getCurrent().getPrecip_mm()));
                    String iconUrl ="https:"+weather.getCurrent().getCondition().getIcon();
                    Glide.with(MainActivity.this)
                            .load(iconUrl)
                            .into(icon);
                }else {
                    Toast.makeText(MainActivity.this, "City not found or API error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                Log.e("RetrofitError",t.getMessage());

            }
        });
    }

    private void fetchWeatherHour(String cityName) {
        WeatherApiService apiService = RetrofitClient.getRetrofitInstance().create(WeatherApiService.class);
        Call<WeatherResponse> call = apiService.getWeatherHour(apiKey, cityName,1);
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if(response.isSuccessful()){
                    WeatherResponse weatherResponse = response.body();
                    if (weatherResponse != null && weatherResponse.getForecast() != null) {
                        List<Hour> hourList = weatherResponse.getForecast().getForecastDay().get(0).getHour();
                        hourAdapter = new HourAdapter(MainActivity.this, hourList);
                        recyclerView.setAdapter(hourAdapter);
                    }else {
                        Toast.makeText(MainActivity.this, "Forecast data is missing", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(MainActivity.this, "City not found or API error", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                Log.e("RetrofitError",t.getMessage());

            }
        });
    }

    private void connectXML() {
        editCity = findViewById(R.id.editTextCity);
        btnGetWeather = findViewById(R.id.button);
        tempText = findViewById(R.id.temperature);
        humidityText = findViewById(R.id.humidity);
        windText = findViewById(R.id.wind);
        rainText = findViewById(R.id.rain);
        icon = findViewById(R.id.icon);
        recyclerView = findViewById(R.id.recycleHour);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
    }
}