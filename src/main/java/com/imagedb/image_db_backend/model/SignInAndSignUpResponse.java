package com.imagedb.image_db_backend.model;

public class SignInAndSignUpResponse {
    private String token;
    private String message;

    public SignInAndSignUpResponse() {
    }

    public SignInAndSignUpResponse(String token, String message) {
        this.token = token;
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
