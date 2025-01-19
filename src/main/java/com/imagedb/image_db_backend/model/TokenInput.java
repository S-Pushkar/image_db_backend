package com.imagedb.image_db_backend.model;

public class TokenInput {
    private String token;

    public TokenInput() {
    }

    public TokenInput(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
