package com.example.finalexample.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "history_city")
public class History {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @ColumnInfo(name = "cityID")
    private String locationID;
    private String cityName;
    @ColumnInfo(name = "Date")
    private String date;
    private String TextDay;
    private String TextNight;
    private String TempMin;
    private String TempMax;

    public History() {
    }

    public History(String locationID,String cityName,String date, String textDay, String textNight, String tempMin, String tempMax) {
        this.locationID=locationID;
        this.cityName=cityName;
        this.date = date;
        TextDay = textDay;
        TextNight = textNight;
        TempMin = tempMin;
        TempMax = tempMax;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTextDay() {
        return TextDay;
    }

    public void setTextDay(String textDay) {
        TextDay = textDay;
    }

    public String getTextNight() {
        return TextNight;
    }

    public void setTextNight(String textNight) {
        TextNight = textNight;
    }

    public String getTempMin() {
        return TempMin;
    }

    public void setTempMin(String tempMin) {
        TempMin = tempMin;
    }

    public String getTempMax() {
        return TempMax;
    }

    public void setTempMax(String tempMax) {
        TempMax = tempMax;
    }

    public String getLocationID() {
        return locationID;
    }

    public void setLocationID(String locationID) {
        this.locationID = locationID;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
