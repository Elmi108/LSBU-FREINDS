package com.android.lsbufriends.data.model.register;

public class RegisterRequest {
    String firstName, lastName, faculty, mail, password, phoneNumber, uid;

    public RegisterRequest() {
    }

    public RegisterRequest(String firstName, String lastName, String faculty, String mail, String password, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.faculty = faculty;
        this.mail = mail;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }

    public RegisterRequest(String firstName, String lastName, String faculty, String mail, String password, String phoneNumber, String uid) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.faculty = faculty;
        this.mail = mail;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
