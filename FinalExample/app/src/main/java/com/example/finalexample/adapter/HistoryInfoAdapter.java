package com.example.finalexample.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalexample.R;

import java.util.List;

public class HistoryInfoAdapter extends RecyclerView.Adapter<HistoryInfoAdapter.VH> {
    private List<HistoryInfo> mlist;
    public HistoryInfoAdapter(List<HistoryInfo> list){
        super();
        this.mlist=list;
    }
    public static class VH extends RecyclerView.ViewHolder{
        public TextView city;
        public TextView date;
        public TextView info;
        public VH(View itemView){
            super(itemView);
            city=itemView.findViewById(R.id.tv_his_city);
            date=itemView.findViewById(R.id.tv_his_date);
            info=itemView.findViewById(R.id.tv_his_info);
        }
    }
    @NonNull
    @Override
    public HistoryInfoAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_history,parent,false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryInfoAdapter.VH holder, int position) {
        holder.city.setText(mlist.get(position).getCity());
        holder.city.setTextColor(Color.rgb(54,54,54));
        holder.date.setText(mlist.get(position).getDate());
        holder.date.setTextColor(Color.rgb(54,54,54));
        holder.info.setText(mlist.get(position).getInfo());
        holder.info.setTextColor(Color.rgb(54,54,54));
    }

    @Override
    public int getItemCount() {
        return mlist.size();
    }
}
