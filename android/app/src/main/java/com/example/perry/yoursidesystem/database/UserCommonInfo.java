package com.example.perry.yoursidesystem.database;

import org.litepal.crud.DataSupport;

/**
 * Created by perry on 2018/3/11.
 */

public class UserCommonInfo extends DataSupport {
    private int id;
    private String userName;
    private String sex;
    private String weight;
    private String height;


    public UserCommonInfo(String userName, String sex, String weight, String height) {
        this.userName = userName;
        this.sex = sex;
        this.weight = weight;
        this.height = height;
    }

    public UserCommonInfo(int id) {
        this.id = id;
    }

    public UserCommonInfo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }
}
