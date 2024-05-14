package com.example.appll;

public class WillInfo {
    private String yourId;
    private String yourName;
    private String recipientId;
    private String recipientName;

    public WillInfo() {
        // Default constructor required for Firebase
    }

    public WillInfo(String yourId, String yourName, String recipientId, String recipientName) {
        this.yourId = yourId;
        this.yourName = yourName;
        this.recipientId = recipientId;
        this.recipientName = recipientName;
    }

    public String getYourId() {
        return yourId;
    }

    public void setYourId(String yourId) {
        this.yourId = yourId;
    }

    public String getYourName() {
        return yourName;
    }

    public void setYourName(String yourName) {
        this.yourName = yourName;
    }

    public String getRecipientId() {
        return recipientId;
    }

    public void setRecipientId(String recipientId) {
        this.recipientId = recipientId;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }
}

