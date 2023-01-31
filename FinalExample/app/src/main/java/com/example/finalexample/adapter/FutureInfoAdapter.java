package com.example.finalexample.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalexample.R;

import java.util.List;

public class FutureInfoAdapter extends RecyclerView.Adapter<FutureInfoAdapter.VH>{
    private List<FutureIcon> iconList;
    public FutureInfoAdapter(List<FutureIcon> list) {
        super();
        this.iconList = list;
    }
    public static class VH extends RecyclerView.ViewHolder{
        public ImageView icon_future;
        public TextView date;
        public TextView future_info;
        public VH(View itemView){
            super(itemView);
            icon_future=itemView.findViewById(R.id.icon_future);
            date=itemView.findViewById(R.id.date);
            future_info=itemView.findViewById(R.id.future_info);
        }
    }
    @NonNull
    @Override
    public FutureInfoAdapter.VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_futrue,parent,false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull FutureInfoAdapter.VH holder, int position) {
        holder.date.setText(iconList.get(position).getDate());
        holder.icon_future.setBackgroundResource(iconList.get(position).getFuture_icon());
        holder.future_info.setText(iconList.get(position).getFuture_info());
    }

    @Override
    public int getItemCount() {
        return  iconList.size();
    }
}
