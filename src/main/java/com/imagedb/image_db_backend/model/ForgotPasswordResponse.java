package com.imagedb.image_db_backend.model;

public class ForgotPasswordResponse {
    private String message;

    public ForgotPasswordResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
