package com.example.promingle.Model;

public class MessageModel {
    String message;

    String SenderId;
    String Attachments;
    long TimeStamp;

    public MessageModel() {
    }

    public MessageModel(String message, String senderId, long timeStamp) {
        this.message = message;
        SenderId = senderId;
        TimeStamp = timeStamp;
    }

    public MessageModel(String message, String senderId, String attachments, long timeStamp) {
        this.message = message;
        SenderId = senderId;
        Attachments = attachments;
        TimeStamp = timeStamp;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return SenderId;
    }

    public void setSenderId(String senderId) {
        SenderId = senderId;
    }
    public String getAttachments() {
        return Attachments;
    }

    public void setAttachments(String attachments) {
        Attachments = attachments;
    }

    public long getTimeStamp() {
        return TimeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        TimeStamp = timeStamp;
    }
}
