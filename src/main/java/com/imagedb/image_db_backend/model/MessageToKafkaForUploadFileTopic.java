package com.imagedb.image_db_backend.model;

import java.util.Base64;

public class MessageToKafkaForUploadFileTopic {

    private String email;
    private String fileName;
    private String fileContent;

    public MessageToKafkaForUploadFileTopic() {
    }

    public MessageToKafkaForUploadFileTopic(String email, String fileName, byte[] fileContent) {
        this.email = email;
        this.fileName = fileName;
        this.fileContent = Base64.getEncoder().encodeToString(fileContent);
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

    public byte[] getFileContent() {
        return Base64.getDecoder().decode(fileContent);
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = Base64.getEncoder().encodeToString(fileContent);
    }
}
