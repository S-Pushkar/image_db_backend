package com.imagedb.image_db_backend.model;

public class RequestForQueryImage {

    private String token;
    private String query;

    public RequestForQueryImage() {
    }

    public RequestForQueryImage(String token, String query) {
        this.token = token;
        this.query = query;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }
}
