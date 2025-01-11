package com.example.weather_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.weather_app.Model.Hour;
import com.example.weather_app.R;

import java.util.List;

public class HourAdapter extends RecyclerView.Adapter<HourAdapter.HourViewHolder> {

    private Context context;
    private List<Hour> hourList;

    // Constructor
    public HourAdapter(Context context, List<Hour> hourList) {
        this.context = context;
        this.hourList = hourList;
    }

    @NonNull
    @Override
    public HourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout item_hour.xml
        View view = LayoutInflater.from(context).inflate(R.layout.viewholder_hourly, parent, false);
        return new HourViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HourViewHolder holder, int position) {
        // Get current Hour object
        Hour hour = hourList.get(position);

        // Set time
        String[] parts = hour.getTime().split(" ");
        holder.hourTxt.setText(parts[1]); // Lấy phần giờ (HH:mm) từ chuỗi

        // Set temperature
        holder.tempHour.setText(String.format("%s°C", hour.getTemp_c()));
        // Load weather icon (using Glide for network images)
        if (hour.getCondition() != null && hour.getCondition().getIcon() != null) {
            String iconResID = "https:" + hour.getCondition().getIcon();
            Glide.with(context).load(iconResID).into(holder.iconHour);
        } else {
            holder.iconHour.setImageResource(R.drawable.cloudy_2); // Icon mặc định
        }

    }

    @Override
    public int getItemCount() {
        return hourList == null ? 0 : hourList.size();
    }

    // ViewHolder class
    public static class HourViewHolder extends RecyclerView.ViewHolder {
        TextView hourTxt, tempHour;
        ImageView iconHour;

        public HourViewHolder(@NonNull View itemView) {
            super(itemView);
            hourTxt = itemView.findViewById(R.id.hourTxt);
            tempHour = itemView.findViewById(R.id.tempHour);
            iconHour = itemView.findViewById(R.id.iconHour);
        }
    }
}
