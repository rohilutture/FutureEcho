package com.example.appll;
public class MessageData {
    private String title;
    private String message;
    private String imageUri; // URL to store the image
    private String dateTime;
    private String userId;

    // Constructors
    public MessageData() {
        // Default constructor required for calls to DataSnapshot.getValue(MessageData.class)
    }

    public MessageData(String title, String message, String imageUri, String dateTime, String userId) {
        this.title = title;
        this.message = message;
        this.imageUri = imageUri;
        this.dateTime = dateTime;
        this.userId = userId;
    }

    // Getter methods
    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getImageUri() {
        return imageUri;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getUserId() {
        return userId;
    }
    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}

