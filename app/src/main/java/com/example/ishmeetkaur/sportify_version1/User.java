package com.example.ishmeetkaur.sportify_version1;

/**
 * Created by dell on 4/5/2018.
 */

public class User {
    private String userName;
    private String userEmail;
    private String userNumber;
    private String userGender;


    public User(String userName, String userEmail, String userNumber, String userGender) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userNumber = userNumber;
        this.userGender = userGender;
    }

    public User() {
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    @Override
    public String toString() {
        return userName + "," + userEmail + "," + userNumber + "," + userGender;
    }
}
