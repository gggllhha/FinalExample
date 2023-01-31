package com.example.finalexample.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalexample.R;

import java.util.List;

public class WeatherInfoAdapter extends RecyclerView.Adapter<WeatherInfoAdapter.VH>{
    private List<Icon> iconList;
    public WeatherInfoAdapter(List<Icon> list) {
        super();
        this.iconList = list;
    }
    public static class VH extends RecyclerView.ViewHolder{
        public ImageView icon;
        public TextView title;
        public TextView info;
        public VH(View itemView){
            super(itemView);
            icon=itemView.findViewById(R.id.icon);
            title=itemView.findViewById(R.id.title);
            info=itemView.findViewById(R.id.info);
        }
    }
    @NonNull
    @Override
    public WeatherInfoAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weatherinfo,parent,false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherInfoAdapter.VH holder, int position) {
        holder.title.setText(iconList.get(position).getTitle());
        holder.title.setTextColor(Color.rgb(54,54,54));
        holder.icon.setBackgroundResource(iconList.get(position).getIcon());
        holder.info.setText(iconList.get(position).getInfo());
        holder.info.setTextColor(Color.rgb(54,54,54));
    }

    @Override
    public int getItemCount() {
        return  iconList.size();
    }
}
