package com.imagedb.image_db_backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.imagedb.image_db_backend.model.UserSchema;

@Repository
public interface UserRepository extends MongoRepository<UserSchema, String> {
    UserSchema findByEmail(String email);
}
