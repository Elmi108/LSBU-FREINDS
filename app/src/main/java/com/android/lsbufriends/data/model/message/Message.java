package com.android.lsbufriends.data.model.message;

public class Message {
    String messageId,message,senderId,imageUrl;
    Long timestamp;

    public Message() {
    }

    public Message(String messageId, String message, String senderId, String imageUrl, Long timestamp) {
        this.messageId = messageId;
        this.message = message;
        this.senderId = senderId;
        this.imageUrl = imageUrl;
        this.timestamp = timestamp;
    }

    public Message(String message, String senderId, Long timestamp) {
        this.message = message;
        this.senderId = senderId;
        this.timestamp = timestamp;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
