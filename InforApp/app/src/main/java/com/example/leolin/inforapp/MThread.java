package com.example.leolin.inforapp;

public class MThread {
    private int icon;
    private String name;
    private String questionBody;
    private String currentTime;
    private String postTimes;

    public MThread(int icon,String name,String questionBody,String currentTime,String postTimes){
        this.icon = icon;
        this.name = name;
        this.questionBody = questionBody;
        this.currentTime = currentTime;
        this.postTimes = postTimes;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public void setQuestionBody(String questionBody) {
        this.questionBody = questionBody;
    }

    public void setPostTimes(String postTimes){
        this.postTimes= postTimes;
    }

    public int getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public String getQuestionBody() {
        return questionBody;
    }

    public String getPostTimes() {
        return postTimes;
    }

}
