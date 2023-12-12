package com.client.client.models;

public record EmailItem(String displayString, int id) {

    @Override
    public String toString() {
        return displayString;
    }
}