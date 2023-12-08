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
    private List<String> recipient;
    private String text;
    private String title;
    private Date date;
    private boolean isRead;

    // Constructor
    public Email(String sender, List<String> recipient, String text, String title, Date date, boolean isRead) {
        this.id = 0;
        this.sender = sender;
        this.recipient = recipient;
        this.text = text;
        this.title = title;
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

    public List<String> getRecipient() {
        return recipient;
    }

    public void setRecipients(List<String > recipient) {
        this.recipient = recipient;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
        return username.matches("^(?=.{1,64}@)[A-Za-z0-9_-]+(\\\\.[A-Za-z0-9_-]+)*@[^-][A-Za-z0-9-]+(\\\\.[A-Za-z0-9-]+)*(\\\\.[A-Za-z]{2,})$");
    }

}