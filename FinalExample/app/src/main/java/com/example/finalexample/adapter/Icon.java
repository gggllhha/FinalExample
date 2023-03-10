package com.example.finalexample.adapter;

public class Icon {
    private int icon;
    private String title;
    private String info;
    public Icon(int icon, String title,String info) {
        this.icon = icon;
        this.title = title;
        this.info=info;
    }

    public Icon() {
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
