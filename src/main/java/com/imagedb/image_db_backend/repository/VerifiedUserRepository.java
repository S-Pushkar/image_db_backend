package com.imagedb.image_db_backend.repository;

import com.imagedb.image_db_backend.model.VerifiedUserSchema;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VerifiedUserRepository extends MongoRepository<VerifiedUserSchema, String> {
    VerifiedUserSchema findByEmail(String email);
}
