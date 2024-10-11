package com.imagedb.image_db_backend.model;

public class RequestToQueryFileNameAPI {

    private String email;
    private String text;

    public RequestToQueryFileNameAPI() {
    }

    public RequestToQueryFileNameAPI(String email, String query) {
        this.email = email;
        this.text = query;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
