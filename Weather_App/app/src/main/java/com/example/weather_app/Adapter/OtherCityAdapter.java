package com.example.weather_app.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.weather_app.Model.CityWeather;

import com.example.weather_app.R;

import java.util.List;

public class OtherCityAdapter  extends RecyclerView.Adapter<OtherCityAdapter.CityViewHolder>{
    private Context context;
    private List<CityWeather> cityList;

    public OtherCityAdapter(Context context, List<CityWeather> cityList) {
        this.context = context;
        this.cityList = cityList;
    }

    @NonNull
    @Override
    public OtherCityAdapter.CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_othercity,parent,false);
        return new CityViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull OtherCityAdapter.CityViewHolder holder, int position) {
        CityWeather cityWeather = cityList.get(position);
        holder.cityTxt.setText(cityWeather.getCityName());
        if (cityWeather.getCondition() != null && cityWeather.getCondition().getIcon() != null){
            String resID = "https:" + cityWeather.getCondition().getIcon();
            Glide.with(context).load(resID).into(holder.iconCity);
        }else{
            holder.iconCity.setImageResource(R.drawable.cloudy_2); // Icon mặc định
        }
        holder.tempCity.setText(cityWeather.getMinTemp() + "° / " + cityWeather.getMaxTemp() + "°C");
    }

    @Override
    public int getItemCount() {
        return cityList == null ? 0 : cityList.size();
    }


    public static class CityViewHolder extends RecyclerView.ViewHolder {
        TextView cityTxt, tempCity;
        ImageView iconCity;

        public CityViewHolder(@NonNull View itemView) {
            super(itemView);
            cityTxt = itemView.findViewById(R.id.cityTxt);
            tempCity = itemView.findViewById(R.id.tempCity);
            iconCity = itemView.findViewById(R.id.iconCity);
        }
    }
}
