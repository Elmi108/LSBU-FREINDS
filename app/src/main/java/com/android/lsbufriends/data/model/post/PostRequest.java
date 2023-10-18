package com.android.lsbufriends.data.model.post;

public class PostRequest {
    String publisher, postID, message, timestamp,faculty;

    public PostRequest() {
    }

    public PostRequest(String publisher, String postID, String message, String timestamp, String faculty) {
        this.publisher = publisher;
        this.postID = postID;
        this.message = message;
        this.timestamp = timestamp;
        this.faculty = faculty;
    }

    public PostRequest(String publisher, String postID, String message, String timestamp) {
        this.publisher = publisher;
        this.postID = postID;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
