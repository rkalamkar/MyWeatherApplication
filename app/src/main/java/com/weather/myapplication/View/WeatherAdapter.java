package com.weather.myapplication.View;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.weather.myapplication.Process.API.Model.forecastday;
import com.weather.myapplication.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WeatherAdapter extends RecyclerView.Adapter {

    ArrayList<forecastday> list;
    Context context;

    public WeatherAdapter(Context context, ArrayList<forecastday> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item, viewGroup, false);
        return new WeatherHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if (viewHolder instanceof WeatherHolder) {
            WeatherHolder weatherHolder = (WeatherHolder) viewHolder;
            weatherHolder.txtDay.setText(getDayOfWeek(list.get(i).getDate()));
            weatherHolder.txtTemp.setText(list.get(i).getDay().getAvgtemp_c() + " C"/*°*/);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public String getDayOfWeek(String dtStart) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = format.parse(dtStart);
            String dayOfTheWeek = (String) DateFormat.format("EEEE", date);
            return dayOfTheWeek;
        } catch (ParseException e) {
            e.printStackTrace();
            return dtStart;
        }
    }


    private class WeatherHolder extends RecyclerView.ViewHolder {
        TextView txtDay, txtTemp;

        public WeatherHolder(@NonNull View itemView) {
            super(itemView);
            txtDay = (TextView) itemView.findViewById(R.id.textViewDay);
            txtTemp = (TextView) itemView.findViewById(R.id.textViewTempDay);
        }
    }
}
