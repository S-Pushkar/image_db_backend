package com.imagedb.image_db_backend.model;

public class OutputForUploadFile {
    private String message;

    public OutputForUploadFile() {
    }

    public OutputForUploadFile(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
