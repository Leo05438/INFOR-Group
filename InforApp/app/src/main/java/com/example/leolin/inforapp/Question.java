package com.example.leolin.inforapp;

import java.io.Serializable;

public class Question implements Serializable{

    private int asker;
    private String username;
    private String qtitle;
    private String qtag;

    public Question(int asker,String qtitle,String qtag,String username){
        super();
        this.asker = asker;
        this.qtitle = qtitle;
        this.qtag = qtag;
        this.username = username;
    }
    public int getAsker(){
        return asker;
    }
    public String getQtitle(){
        return qtitle;
    }
    public String getQtag(){
        return qtag;
    }

    public String getUsername() {
        return username;
    }

    public void setAsker(int asker){
        this.asker = asker;
    }
    public void setQtitle(String qtitle){
        this.qtitle = qtitle;
    }
    public void setQtag(String qtag){
        this.qtag = qtag;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
