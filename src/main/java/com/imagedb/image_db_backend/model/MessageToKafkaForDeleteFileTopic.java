package com.imagedb.image_db_backend.model;

public class MessageToKafkaForDeleteFileTopic {

    private String email;
    private String fileName;

    public MessageToKafkaForDeleteFileTopic() {
    }

    public MessageToKafkaForDeleteFileTopic(String email, String fileName) {
        this.email = email;
        this.fileName = fileName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
