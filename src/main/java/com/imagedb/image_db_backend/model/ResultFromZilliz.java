package com.imagedb.image_db_backend.model;

public class ResultFromZilliz {

    private String id;
    private double distance;
    private String email;
    private String timestamp;
    private String image_path;

    public ResultFromZilliz() {
    }

    public ResultFromZilliz(String id, double distance, String email, String timestamp, String imagePath) {
        this.id = id;
        this.distance = distance;
        this.email = email;
        this.timestamp = timestamp;
        this.image_path = imagePath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }
}
