package com.example.leolin.inforapp;

import android.app.Application;

public class Username extends Application {
    private String USERNAME;
    private String PASSWORD;

    public String getUSERNAME(){
        return USERNAME;
    }

    public String getPASSWORD() {
        return PASSWORD;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public void setPASSWORD(String PASSWORD) {
        this.PASSWORD = PASSWORD;
    }

    @Override
    public void onCreate(){
        USERNAME = "";
        PASSWORD = "";
        super.onCreate();
    }
}
