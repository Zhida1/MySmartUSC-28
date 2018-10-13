package com.example.zhidachen.mysmartusc_28;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.ArrayList;

@Entity
public class User {

    @PrimaryKey
    private String username;
    private ArrayList<Keyword> keywords = new ArrayList<Keyword>();
    private ArrayList<Notification> notifications = new ArrayList<Notification>();

    public User(){

    }

    public User(String username){

        this.username = username;
    }

    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {

        this.username = username;
    }

    public ArrayList<Keyword> getKeywords() {

        return keywords;
    }

    public boolean checkKeyword(String keyword, String checkArea) {
        for(Keyword key : keywords) {
            if(key.getKeyword().equals(keyword)){
                if(key.getCheckArea().equals(checkArea)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setKeywords(ArrayList<Keyword> keywords) {

        this.keywords = keywords;
    }

    public ArrayList<Notification> getNotifications() {

        return notifications;
    }

    public void setNotifications(ArrayList<Notification> notifications) {

        this.notifications = notifications;
    }
}
