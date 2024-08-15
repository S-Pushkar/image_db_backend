package com.imagedb.image_db_backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "otps")
public class OTPSchema {
    @Id
    private String id;
    private String email;
    private String otp;
    private final Date timestamp;
    @Indexed(name = "expireAtIndex", expireAfterSeconds = 0)
    private final Date expireAt;

    public OTPSchema(String email, String otp) {
        this.email = email;
        this.otp = otp;
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

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public Date getExpireAt() {
        return expireAt;
    }
}
