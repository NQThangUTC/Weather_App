package com.example.weather_app.Activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.weather_app.API.RetrofitClient;
import com.example.weather_app.API.WeatherApiService;
import com.example.weather_app.Adapter.HourAdapter;
import com.example.weather_app.Adapter.OtherCityAdapter;
import com.example.weather_app.Model.CityWeather;
import com.example.weather_app.Model.Condition;
import com.example.weather_app.Model.Hour;
import com.example.weather_app.Model.WeatherResponse;
import com.example.weather_app.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private EditText editCity;
    private Button btnGetWeather;
    private TextView tempText, humidityText,windText, rainText,conditionText,minMaxTemp;
    private ImageView icon;
    private RecyclerView recyclerView, recyclerCity;
    private HourAdapter hourAdapter;
    private OtherCityAdapter cityAdapter;
    private List<CityWeather> cityWeatherList;
    private final String apiKey = "db5c9522a88247b79f672526251001";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private final List<String> provinces = new ArrayList<>(List.of(
            "Hà Nội", "Hồ Chí Minh", "Đà Nẵng", "Hải Phòng", "Cần Thơ",
            "Bắc Giang", "Bắc Ninh", "Thái Nguyên", "Hà Giang", "Cao Bằng",
            "Lạng Sơn", "Quảng Ninh", "Nam Định", "Thái Bình", "Ninh Bình",
            "Thanh Hóa", "Nghệ An", "Hà Tĩnh", "Quảng Bình", "Quảng Trị",
            "Huế", "Quảng Nam", "Quảng Ngãi", "Bình Định", "Phú Yên",
            "Khánh Hòa", "Ninh Thuận", "Bình Thuận", "Kon Tum", "Gia Lai",
            "Đắk Lắk", "Đắk Nông", "Lâm Đồng", "Bình Phước", "Tây Ninh",
            "Bình Dương", "Đồng Nai", "Bà Rịa - Vũng Tàu", "Long An",
            "Tiền Giang", "Bến Tre", "Trà Vinh", "Vĩnh Long", "Đồng Tháp",
            "An Giang", "Kiên Giang", "Cà Mau", "Bạc Liêu", "Sóc Trăng",
            "Hậu Giang"));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectXML();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        checkPermission();
        cityWeatherList = new ArrayList<>();
        cityAdapter = new OtherCityAdapter(this, cityWeatherList); // Tạo adapter mới
        recyclerCity.setAdapter(cityAdapter);
        List<String> randomProvinces = getRandomProvinces(10);
        fetchWeatherForCities(randomProvinces);
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
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
            return;
        }

        fusedLocationProviderClient.getLastLocation()
                .addOnSuccessListener(location -> {
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        String gps = String.valueOf(latitude)+","+String.valueOf(longitude);
                        fetchWeather(gps);
                        fetchWeatherHour(gps);
                    } else {
                        Toast.makeText(this, "Unable to fetch location", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to fetch location", Toast.LENGTH_SHORT).show();
                    Log.e("GPS Error", e.getMessage());
                });
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Yêu cầu quyền truy cập vị trí
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            // Quyền đã được cấp, lấy vị trí
            getCurrentLocation();
        }
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
                        conditionText.setText(weatherResponse.getCurrent().getCondition().getText());
                        double maxTemp = weatherResponse.getForecast().getForecastDay().get(0).getDay().getMaxTemp();
                        double minTemp = weatherResponse.getForecast().getForecastDay().get(0).getDay().getMinTemp();
                        if (maxTemp != 0 && minTemp != 0){
                            String minMax = String.valueOf(minTemp)+'°'+'/'+String.valueOf(maxTemp)+'°';
                            minMaxTemp.setText(minMax);
                        }else {
                            Toast.makeText(MainActivity.this,"ERROR MIN MAX TEMP",Toast.LENGTH_SHORT).show();
                        }

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

    private void fetchWeatherForCities(List<String> cities) {
        WeatherApiService apiService = RetrofitClient.getRetrofitInstance().create(WeatherApiService.class);

        for (String city : cities) {
            apiService.getWeatherCity(apiKey, city, 1).enqueue(new Callback<WeatherResponse>() {
                @Override
                public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        WeatherResponse weatherResponse = response.body();
                        CityWeather cityWeather = new CityWeather();
                        cityWeather.setCityName(city);
                        cityWeather.setCondition(new Condition(weatherResponse.getCurrent().getCondition().getText(),weatherResponse.getCurrent().getCondition().getIcon()));
                        cityWeather.setMinTemp(weatherResponse.getForecast().getForecastDay().get(0).getDay().getMinTemp());
                        cityWeather.setMaxTemp(weatherResponse.getForecast().getForecastDay().get(0).getDay().getMaxTemp());

                        cityWeatherList.add(cityWeather);
                        if (cityWeatherList.isEmpty()) {
                            Toast.makeText(MainActivity.this, "No data available for cities", Toast.LENGTH_SHORT).show();
                        } else {
                            cityAdapter.notifyDataSetChanged(); // Cập nhật RecyclerView nếu danh sách không rỗng
                        }

                    } else {
                        Log.e("API Error", "Failed to fetch data for city: " + city);
                    }
                }

                @Override
                public void onFailure(Call<WeatherResponse> call, Throwable t) {
                    Log.e("API Error", "Failed to connect: " + t.getMessage());
                }
            });
        }
    }
    private List<String> getRandomProvinces(int count) {
        List<String> shuffledProvinces = new ArrayList<>(provinces);
        Collections.shuffle(shuffledProvinces);
        return shuffledProvinces.subList(0, count);
    }
    private void connectXML() {
        editCity = findViewById(R.id.editTextCity);
        btnGetWeather = findViewById(R.id.button);
        tempText = findViewById(R.id.temperature);
        humidityText = findViewById(R.id.humidity);
        windText = findViewById(R.id.wind);
        rainText = findViewById(R.id.rain);
        conditionText = findViewById(R.id.conditionText);
        minMaxTemp = findViewById(R.id.minMaxTemp);
        icon = findViewById(R.id.icon);
        recyclerView = findViewById(R.id.recycleHour);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
        recyclerCity = findViewById(R.id.otherCityRecycler);
        recyclerCity.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));
    }
}