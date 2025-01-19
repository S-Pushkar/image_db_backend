package com.imagedb.image_db_backend.model;

public class RequestForDeleteImage {

    private String token;
    private String imageName;

    public RequestForDeleteImage() {
    }

    public RequestForDeleteImage(String token, String imageName) {
        this.token = token;
        this.imageName = imageName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}
