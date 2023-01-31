package com.example.finalexample.adapter;

public class FutureIcon {
    private int future_icon;
    private String date;
    private String future_info;

    public FutureIcon() {
    }

    public FutureIcon(int future_icon, String date, String future_info) {
        this.future_icon = future_icon;
        this.date = date;
        this.future_info = future_info;
    }

    public int getFuture_icon() {
        return future_icon;
    }

    public void setFuture_icon(int future_icon) {
        this.future_icon = future_icon;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFuture_info() {
        return future_info;
    }

    public void setFuture_info(String future_info) {
        this.future_info = future_info;
    }
}
