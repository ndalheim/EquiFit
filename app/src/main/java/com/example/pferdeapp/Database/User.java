package com.example.pferdeapp.Database;

import android.widget.ImageView;

public class User {

    private String userName;
    private String email;
    private String password;
    private ImageView userImage;

    public User(String userName, String email, String password, ImageView userImage) {

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ImageView getUserImage() {
        return userImage;
    }

    public void setUserImage(ImageView userImage) {
        this.userImage = userImage;
    }
}


