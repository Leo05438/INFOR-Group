package com.example.leolin.inforapp;

public class MItem {
    private int iId;
    private String iName;

    public MItem() {
    }

    public MItem(String iName) {
        this.iName = iName;
    }

    public String getiName() {
        return iName;
    }

    public void setiName(String iName) {
        this.iName = iName;
    }
}
