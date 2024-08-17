package com.imagedb.image_db_backend.model;

public class SignInDetails {
    private String email;
    private String password;

    public SignInDetails() {
    }

    public SignInDetails(String email, String password) {
        this.email = email;
        this.password = password;
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
}

