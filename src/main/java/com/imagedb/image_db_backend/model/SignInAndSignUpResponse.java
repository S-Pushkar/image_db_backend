package com.imagedb.image_db_backend.model;

public class SignInAndSignUpResponse {
    private String token;

    public SignInAndSignUpResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
