package com.imagedb.image_db_backend.service;

import com.imagedb.image_db_backend.model.OTPSchema;
import com.imagedb.image_db_backend.repository.OTPRepository;
import org.springframework.stereotype.Service;

@Service
public class OTPService {

    private final OTPRepository otpRepository;

    public OTPService(OTPRepository otpRepository) {
        this.otpRepository = otpRepository;
    }

    public void saveOTP(String email, String otp) {
        otpRepository.save(new OTPSchema(email, otp));
    }

    public OTPSchema getOTPByEmail(String email) {
        return otpRepository.findByEmail(email);
    }

    public void deleteOTPByEmail(String email) {
        OTPSchema otp = otpRepository.findByEmail(email);
        if (otp != null) {
            otpRepository.delete(otp);
        }
    }

    public void createOTP(OTPSchema otp) {
        otpRepository.save(otp);
    }

    public void updateOTP(OTPSchema otp) {
        otpRepository.save(otp);
    }

}
