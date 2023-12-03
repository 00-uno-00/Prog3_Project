package com.client.client.models;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class Email {
    private String id;
    private String sender;
    private String recipient;
    private String body;
    private String subject;
    private Date date;
    private boolean isRead;

    // Constructor
    public Email(String sender, String recipient, String body, String subject, Date date, boolean isRead) {
        this.id = "0";
        this.sender = sender;
        this.recipient = recipient;
        this.body = body;
        this.subject = subject;
        this.date = date;
        this.isRead = isRead;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = this.body;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

}