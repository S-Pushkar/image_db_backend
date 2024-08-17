package com.imagedb.image_db_backend.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.imagedb.image_db_backend.model.OTPSchema;

@Repository
public interface OTPRepository extends MongoRepository<OTPSchema, String> {
    OTPSchema findByEmail(String email);
}
