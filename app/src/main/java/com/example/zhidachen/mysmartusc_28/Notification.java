package com.example.zhidachen.mysmartusc_28;

public class Notification {
    private String sender;
    private String subject;
    private String type;
    private String emailId;

    public Notification() {

    }

    public Notification(String sender, String subject, String type, String emailID) {
        this.sender = sender;
        this.subject = subject;
        this.type = type;
        this.emailId = emailID;
    }

    public String getSender() {
        return sender;
    }

    public String getSubject() {
        return subject;
    }

    public String getType() {
        return type;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}
