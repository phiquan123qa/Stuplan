package com.vn.Models;

public class User {
    String userID;
    String name;
    String avataURL;

    public User(String userID, String name, String avataURL) {
        this.userID = userID;
        this.name = name;
        this.avataURL = avataURL;
    }

    public User() {
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvataURL() {
        return avataURL;
    }

    public void setAvataURL(String avataURL) {
        this.avataURL = avataURL;
    }
}
