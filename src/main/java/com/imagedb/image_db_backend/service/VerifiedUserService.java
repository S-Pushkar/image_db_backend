package com.imagedb.image_db_backend.service;

import com.imagedb.image_db_backend.model.VerifiedUserSchema;
import com.imagedb.image_db_backend.repository.VerifiedUserRepository;
import org.springframework.stereotype.Service;

@Service
public class VerifiedUserService {
    private final VerifiedUserRepository verifiedUserRepository;

    public VerifiedUserService(VerifiedUserRepository verifiedUserRepository) {
        this.verifiedUserRepository = verifiedUserRepository;
    }

    public void saveVerifiedUser(String email) {
        verifiedUserRepository.save(new VerifiedUserSchema(email));
    }

    public VerifiedUserSchema getVerifiedUserByEmail(String email) {
        return verifiedUserRepository.findByEmail(email);
    }

    public void deleteVerifiedUserByEmail(String email) {
        VerifiedUserSchema verifiedUser = verifiedUserRepository.findByEmail(email);
        if (verifiedUser != null) {
            verifiedUserRepository.delete(verifiedUser);
        }
    }

    public void updateVerifiedUser(VerifiedUserSchema verifiedUser) {
        verifiedUserRepository.save(verifiedUser);
    }
}
