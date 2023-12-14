package com.server.server.models;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Email implements Serializable {

    @Serial
    private static final long serialVersionUID = 5950169519310163575L;
    private int id;
    private String sender;
    private List<String> recipients;
    private String body;
    private String subject;
    private Date date;
    private boolean isRead;

    public Email() {
    }
    // Constructor
    public Email(String sender, List<String> recipients, String body, String subject, Date date, boolean isRead) {
        this.id = 0;
        this.sender = sender;
        this.recipients = recipients;
        this.body = body;
        this.subject = subject;
        this.date = date;
        this.isRead = isRead;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public List<String> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<String > recipient) {
        this.recipients = recipient;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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

    // Methods
    /**
     * Marks the email as read
     */
    public void markAsRead() {
        this.isRead = true;
    }

    /**
     * Checks if the username String has the shape of an email address
     * @param username the username to be checked
     * @return true if the username String has the shape of an email address, false otherwise
     */
    public static boolean isValidFormat(String username) {
        //check if the username String has the shape of an email address
        //match the regex pattern for email addresses :
        //It allows numeric values from 0 to 9
        //Both uppercase and lowercase letters from a to z are allowed
        //Underscore "_", hyphen "-", and dot "." are allowed
        //Dot isn’t allowed at the start and end of the local part
        //Consecutive dots aren’t allowed
        //For the local part, a maximum of 64 characters are allowed
        return username.matches("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");
    }
}