package com.example.finalexample.adapter;

public class HistoryInfo {
    private String city;
    private String date;
    private String info;

    public HistoryInfo() {
    }

    public HistoryInfo(String city, String date, String info) {
        this.city = city;
        this.date = date;
        this.info = info;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
