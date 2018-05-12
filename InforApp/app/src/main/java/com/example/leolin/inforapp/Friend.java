package com.example.leolin.inforapp;

public class Friend {
    int icon;
    String name;

    public Friend(int icon,String name){
        super();
        this.icon = icon;
        this.name = name;
    }

    public int getIcon(){
        return icon;
    }
    public String getName(){
        return name;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setName(String name) {
        this.name = name;
    }

}
