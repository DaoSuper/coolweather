package com.coolweather.android.gson;


import com.google.gson.annotations.SerializedName;

public class Hourly {

    @SerializedName("cond_txt")
    public String info;

    @SerializedName("tmp")
    public String temperature;

    @SerializedName("time")
    public String hourly_time;

    public Hourly(String info, String temperature, String hourly_time){
        this.info = info;
        this.temperature = temperature;
        this.hourly_time = hourly_time;
    }
    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHourly_time() {
        return hourly_time;
    }

    public void setHourly_time(String hourly_time) {
        this.hourly_time = hourly_time;
    }
}
