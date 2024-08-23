package com.imagedb.image_db_backend.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "verified-users")
public class VerifiedUserSchema {
    @Id
    private String id;
    private String email;
    private Date timestamp;
    @Indexed(name = "expireAtIndex", expireAfterSeconds = 0)
    private Date expireAt;

    public VerifiedUserSchema(String email) {
        this.email = email;
        this.timestamp = new Date();
        this.expireAt = new Date(this.timestamp.getTime() + 5 * 60 * 1000);
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

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public Date getExpireAt() {
        return expireAt;
    }

    public void setExpireAt(Date expireAt) {
        this.expireAt = expireAt;
    }
}
