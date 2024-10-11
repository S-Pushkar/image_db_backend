package com.imagedb.image_db_backend.model;

public class OutputForQueryImage {

    private String message;
    private String imageContent;

    public OutputForQueryImage() {
    }

    public OutputForQueryImage(String message) {
        this.message = message;
    }

    public OutputForQueryImage(String message, String imageContent) {
        this.message = message;
        this.imageContent = imageContent;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getImageContent() {
        return imageContent;
    }

    public void setImageContent(String imageContent) {
        this.imageContent = imageContent;
    }
}
