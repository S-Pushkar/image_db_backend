package com.imagedb.image_db_backend.model;

public class SignInAndSignUpResponse {
    private String token;
    private String message;
    private String userName;
    private String userEmail;

    public SignInAndSignUpResponse() {
    }

    public SignInAndSignUpResponse(String token, String message, String userName, String userEmail) {
        this.token = token;
        this.message = message;
        this.userName = userName;
        this.userEmail = userEmail;
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

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}
