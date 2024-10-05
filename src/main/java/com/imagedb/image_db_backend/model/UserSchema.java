package com.imagedb.image_db_backend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class UserSchema {
    @Id
    private String id;
    private String name;
    private String email;
    private String password;
    private int numberOfImages;

    public UserSchema(String name, String email, String password, int numberOfImages) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.numberOfImages = numberOfImages;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getNumberOfImages() { return numberOfImages; }

    public void setNumberOfImages(int numberOfImages) {
        this.numberOfImages = numberOfImages;
    }
}
