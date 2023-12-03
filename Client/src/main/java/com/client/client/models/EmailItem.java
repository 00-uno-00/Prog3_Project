package com.client.client.models;

public class EmailItem {
    private String displayString;
    private String id;

    public EmailItem(String displayString, String id) {
        this.displayString = displayString;
        this.id = id;
    }

    public String getDisplayString() {
        return displayString;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return displayString;
    }
}