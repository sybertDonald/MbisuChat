package com.example.sy.infosys_ips.models;

public class User {
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    String status;

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

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String username;
    String email;
    String imageURL;
    String id;


    public User() {

    }

    public User(String username, String email, String imageURL, String id,String status) {
        this.username = username;
        this.email = email;
        this.imageURL = imageURL;
        this.id = id;
        this.status = status;
    }
}

