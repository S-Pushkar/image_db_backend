package com.imagedb.image_db_backend.model;

public class EnterEmailForForgotPasswordRequest {
    private String email;

    public EnterEmailForForgotPasswordRequest() {
    }

    public EnterEmailForForgotPasswordRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
