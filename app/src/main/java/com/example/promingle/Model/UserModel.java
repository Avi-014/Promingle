package com.example.promingle.Model;

import java.io.Serializable;

public class UserModel implements Serializable {
    public String uid;
    public String username;
    public String email;
    public String imageURI;

    public UserModel(String uid, String username, String email, String imageURI) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.imageURI = imageURI;
    }

    public UserModel() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageURI() {
        return imageURI;
    }

    public void setImageURI(String imageURI) {
        this.imageURI = imageURI;
    }

}
