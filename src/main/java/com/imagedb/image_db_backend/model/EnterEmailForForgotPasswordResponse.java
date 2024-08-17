package com.imagedb.image_db_backend.model;

public class EnterEmailForForgotPasswordResponse {
    private String message;

    public EnterEmailForForgotPasswordResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
