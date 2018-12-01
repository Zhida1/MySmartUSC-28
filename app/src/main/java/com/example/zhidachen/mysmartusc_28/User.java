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
    private ArrayList<UserEmail> userEmails;

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
        userEmails = new ArrayList<UserEmail>();
    }

    public User(String username) {
        this.username = username;
        notifKeywords = new ArrayList<Keyword>();
        markFavKeywords = new ArrayList<Keyword>();
        notifications = new ArrayList<Notification>();
        userEmails = new ArrayList<UserEmail>();
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

    public void addNotification(String sender, String subject, String type, String emailID) {
        Notification temp = new Notification(sender, subject, type, emailID);
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

    public String displayTextNotifc() {
        String text = "";
        for(int i = 0; i < notifications.size(); i++) {
            text += "\n\n" + "New Email from: " + notifications.get(i).getSender() + "\nSubject: " + notifications.get(i).getSubject() + "\nType: " + notifications.get(i).getType();
        }
        return text;
    }

    public ArrayList<com.example.zhidachen.mysmartusc_28.Notification> parseEmail() {
        ArrayList<com.example.zhidachen.mysmartusc_28.Notification> store = new ArrayList<com.example.zhidachen.mysmartusc_28.Notification>();
        for(int x = 0; x < userEmails.size(); x++) {
            if(userEmails.get(x).getNewOld() == 0) {
                userEmails.get(x).setNewOld(1);
                for (int i = 0; i < notifKeywords.size(); i++) {
                    com.example.zhidachen.mysmartusc_28.Notification notif = null;
                    if(userEmails.get(x).getSender().equals(notifKeywords.get(i).getKeyword())) {
                        notif = new Notification(userEmails.get(x).getSender(), userEmails.get(x).getSubject(), notifKeywords.get(i).getCheckArea(), userEmails.get(x).getEmail_id());
                    }
                    else if(userEmails.get(x).getSubject().contains(notifKeywords.get(i).getKeyword())) {
                        notif = new Notification(userEmails.get(x).getSender(), userEmails.get(x).getSubject(), notifKeywords.get(i).getCheckArea(), userEmails.get(x).getEmail_id());
                    }
                    else if(userEmails.get(x).getContent().equals(notifKeywords.get(i).getKeyword())) {
                        notif = new Notification(userEmails.get(x).getSender(), userEmails.get(x).getSubject(), notifKeywords.get(i).getCheckArea(), userEmails.get(x).getEmail_id());
                    }
                    if(notif != null) {
                        store.add(notif);
                        notifications.add(notif);
                        break;
                    }
                }
                for(int j = 0; j < markFavKeywords.size(); j++) {
                    if(userEmails.get(x).getContent().contains(markFavKeywords.get(j).getKeyword())) {
                        userEmails.get(x).setFavNot(1);
                    }
                }
            }
        }
        return store;
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

    public ArrayList<UserEmail> getUserEmails() {
        return userEmails;
    }

    public void setUserEmails(ArrayList<UserEmail> userEmails) {
        this.userEmails = userEmails;
    }
    public void addUserEmail(UserEmail mail) {
        boolean flag = true;
        for(UserEmail temp : userEmails) {
            if((temp.getEmail_id().equals(mail.getEmail_id())) && (temp.getSender().equals(mail.getSender())) && (temp.getSubject().equals(mail.getSubject())) && (temp.getContent().equals(mail.getContent()))) {
                flag = false;
                System.out.println("Mail already added, proceed to next");
                break;
            }
        }
        if(flag) {
            userEmails.add(mail);
        }
    }
}
