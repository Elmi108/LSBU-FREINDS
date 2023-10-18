package com.android.lsbufriends.data.model.comment;

public class CommentRequest {
    String comment, publisher, date;

    public CommentRequest() {
    }

    public CommentRequest(String comment, String publisher, String date) {
        this.comment = comment;
        this.publisher = publisher;
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
