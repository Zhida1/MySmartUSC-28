package com.example.zhidachen.mysmartusc_28;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.ArrayList;

@Entity
public class User {

    @PrimaryKey
    @NonNull private String username;

    private ArrayList<Keyword> notifKeywords;
    private ArrayList<Keyword> markFavKeywords;
    private ArrayList<Notification> notifications;

//    public String access_token;
//    public int expires_in;
//    public String scope;
//    public String token_type;
//    public String id_token;

    public User() {
        username = "";
        notifKeywords = new ArrayList<Keyword>();
        markFavKeywords = new ArrayList<Keyword>();
        notifications = new ArrayList<Notification>();
    }

    public User(String username) {
        this.username = username;
        notifKeywords = new ArrayList<Keyword>();
        markFavKeywords = new ArrayList<Keyword>();
        notifications = new ArrayList<Notification>();
    }

    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {

        this.username = username;
    }


    public void addKeyword(String keyword, String checkArea) {
        if(keyword.equals("")) {
            return;
        }
        else {
            String [] tokens = keyword.split("\\s+");
            for(String temp : tokens) {
                Keyword store = new Keyword(temp, checkArea);
                if(checkArea.equals("Career")) {
                    markFavKeywords.add(store);
                }
                else {
                    notifKeywords.add(store);
                }
            }
        }
    }

    public boolean checkKeyword(String keyword, String checkArea) {
        if(checkArea.equals("Career")) {
            for(Keyword key : markFavKeywords) {
                if(key.getKeyword().equals(keyword)){
                    if(key.getCheckArea().equals(checkArea)) {
                        return true;
                    }
                }
            }
        }
        else {
            for(Keyword key : notifKeywords) {
                if(key.getKeyword().equals(keyword)){
                    if(key.getCheckArea().equals(checkArea)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void removeKeyword(String keyword, String checkArea) {
        int count = 0;
        if (checkArea.equals("Career")) {
            for (Keyword key : markFavKeywords) {
                if (key.getKeyword().equals(keyword)) {
                    if (key.getCheckArea().equals(checkArea)) {
                        markFavKeywords.remove(count);
                        return;
                    }
                }
                count += 1;
            }
        } else {
            for (Keyword key : notifKeywords) {
                if (key.getKeyword().equals(keyword)) {
                    if (key.getCheckArea().equals(checkArea)) {
                        notifKeywords.remove(count);
                        return;
                    }
                }
                count += 1;
            }
        }
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

    public Keyword containsNotifKeyword(String emailContent) {
        for (int i = 0; i < notifKeywords.size(); i++) {
            if (emailContent.contains(notifKeywords.get(i).getKeyword())) {
                return notifKeywords.get(i);
            }
        }
        return null;
    }

    public Keyword containsMarkFavKeyword(String emailContent) {
        for (int i = 0; i < markFavKeywords.size(); i++) {
            if (emailContent.contains(markFavKeywords.get(i).getKeyword())) {
                return markFavKeywords.get(i);
            }
        }
        return null;
    }

    public ArrayList<Keyword> getNotifKeywords()
    {
        return notifKeywords;
    }

    public void setNotifKeywords(ArrayList<Keyword> notifKeywords)
    {
        this.notifKeywords = notifKeywords;
    }

    public ArrayList<Keyword> getMarkFavKeywords()
    {
        return markFavKeywords;
    }

    public void setMarkFavKeywords(ArrayList<Keyword> markFavKeywords)
    {
        this.markFavKeywords = markFavKeywords;
    }
}
