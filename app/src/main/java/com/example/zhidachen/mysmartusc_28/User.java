package com.example.zhidachen.mysmartusc_28;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.ArrayList;

@Entity
public class User {

    @PrimaryKey
    @NonNull private String username;

    private ArrayList<Keyword> keywords;
    private ArrayList<Notification> notifications;

    public User() {
        username = "";
        keywords = new ArrayList<Keyword>();
        notifications = new ArrayList<Notification>();
    }

    public User(String username) {
        this.username = username;
        keywords = new ArrayList<Keyword>();
        notifications = new ArrayList<Notification>();
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

    public void addKeyword(String keyword, String checkArea) {
        if(keyword.equals("")) {
            return;
        }
        else {
            String [] tokens = keyword.split("\\s+");
            for(String temp : tokens) {
                Keyword store = new Keyword(temp, checkArea);
                keywords.add(store);
            }
        }
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

    public void removeKeyword(String keyword, String checkArea) {
        int count = 0;
        for(Keyword key : keywords) {
            if(key.getKeyword().equals(keyword)){
                if(key.getCheckArea().equals(checkArea)) {
                    keywords.remove(count);
                    return;
                }
            }
            count += 1;
        }
    }

    public void setKeywords(ArrayList<Keyword> keywords) {

        this.keywords = keywords;
    }

    public ArrayList<Notification> getNotifications() {

        return notifications;
    }

    public void addNotification(String sender, String subject, String type) {
        Notification temp = new Notification(sender, subject, type);
        notifications.add(temp);
    }

    public void setNotifications(ArrayList<Notification> notifications) {

        this.notifications = notifications;
    }
}
