package com.example.leolin.inforapp;

public class Announcement {
    public int publisher;
    public String atitle,adate;

    public Announcement(int publisher,String atitle,String adate){
        this.publisher = publisher;
        this.atitle = atitle;
        this.adate = adate;
    }

    public int getPublisher() {
        return publisher;
    }

    public String getAdate() {
        return adate;
    }

    public String getAtitle() {
        return atitle;
    }

    public void setAdate(String adate) {
        this.adate = adate;
    }

    public void setAtitle(String atitle) {
        this.atitle = atitle;
    }

    public void setPublisher(int publisher) {
        this.publisher = publisher;
    }
}
