package com.example.zhidachen.mysmartusc_28;

public class UserEmail {
    //0 means has not been checked for keyword yet
    //1 means has been checked for keyword
    private int newOld;
    private String email_id;
    private String sender;
    private String subject;
    private String content;

    public UserEmail() {

    }
    public UserEmail(String email_id, String sender, String subject, String content) {
        newOld = 0;
        this.email_id = email_id;
        this.sender = sender;
        this.subject = subject;
        this.content = content;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getNewOld() {
        return newOld;
    }

    public void setNewOld(int newOld) {
        this.newOld = newOld;
    }
}
