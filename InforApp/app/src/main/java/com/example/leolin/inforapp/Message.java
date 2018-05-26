package com.example.leolin.inforapp;

public class Message {
    private int icon;
    private String name;
    private String messageBody;

    public Message(int icon,String name,String messageBody){
        this.icon = icon;
        this.name = name;
        this.messageBody = messageBody;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    public int getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public String getMessageBody() {
        return messageBody;
    }
}
