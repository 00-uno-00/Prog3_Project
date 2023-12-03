package com.client.client.models;

public class EmailItem {
    private String displayString;
    private int id;

    public EmailItem(String displayString, int id) {
        this.displayString = displayString;
        this.id = id;
    }

    public String getDisplayString() {
        return displayString;
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return displayString;
    }
}