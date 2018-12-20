package com.example.perry.yoursidesystem.database;

import com.example.perry.yoursidesystem.MainActivity;

import org.litepal.crud.DataSupport;

import java.util.Date;

/**
 * Created by perry on 2018/3/2.
 */

public class UserBodyInfo extends DataSupport {
    private int id;
    private String name;
    private float temperature;
    private int heartRate;
    private float systolicPre;
    private float diastolicPre;
    private Date date;

    public UserBodyInfo(float systolicPre, float diastolicPre, float temperature, int
            heartRate) {
        this.temperature = temperature;
        this.heartRate = heartRate;
        this.systolicPre = systolicPre;
        this.diastolicPre = diastolicPre;
        date = new Date();
        this.name = MainActivity.userName;
    }

    public UserBodyInfo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public float getSystolicPre() {
        return systolicPre;
    }

    public void setSystolicPre(float systolicPre) {
        this.systolicPre = systolicPre;
    }

    public float getDiastolicPre() {
        return diastolicPre;
    }

    public void setDiastolicPre(float diastolicPre) {
        this.diastolicPre = diastolicPre;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


    @Override
    public String toString() {
        return "名字：" + name + ",低压：" + systolicPre + ",高压：" + diastolicPre +
                ",体温：" + temperature + ",心率：" + heartRate + ",日期：" + date.toString();
    }
}
