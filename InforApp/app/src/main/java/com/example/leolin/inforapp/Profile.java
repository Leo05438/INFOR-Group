package com.example.leolin.inforapp;

public class Profile {
    private int icon;
    private String item;

    public Profile(int icon,String item){
        this.icon = icon;
        this.item = item;
    }

    public int getIcon() {
        return icon;
    }

    public String getItem() {
        return item;
    }


    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setItem(String username) {
        this.item = item;
    }

}
