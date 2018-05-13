package com.example.leolin.inforapp;

import java.io.Serializable;

public class Question implements Serializable{
    public int asker;
    public String qtitle;
    public String qtag;
    private static final long serialVersionUID = 1L;


    public Question(int asker,String qtitle,String qtag){
        super();
        this.asker = asker;
        this.qtitle = qtitle;
        this.qtag = qtag;
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
    public void setAsker(int asker){
        this.asker = asker;
    }
    public void setQtitle(String qtitle){
        this.qtitle = qtitle;
    }
    public void setQtag(String qtag){
        this.qtag = qtag;
    }

}
