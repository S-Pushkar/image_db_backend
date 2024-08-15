package com.imagedb.image_db_backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "images")
public class ImageSchema {
    @Id
    private String id;
    private String email;
    private String url;
    private String tag;
    private final Date timestamp;

    public ImageSchema(String email, String url, String tag) {
        this.email = email;
        this.url = url;
        this.tag = tag;
        this.timestamp = new Date();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
