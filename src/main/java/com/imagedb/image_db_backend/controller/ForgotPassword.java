package com.imagedb.image_db_backend.controller;

import com.imagedb.image_db_backend.model.EnterEmailForForgotPasswordRequest;
import com.imagedb.image_db_backend.model.EnterEmailForForgotPasswordResponse;
import com.imagedb.image_db_backend.model.OTPSchema;
import com.imagedb.image_db_backend.model.UserSchema;
import com.imagedb.image_db_backend.service.EmailService;
import com.imagedb.image_db_backend.service.OTPService;
import com.imagedb.image_db_backend.service.UserService;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@RestController
@RequestMapping("/api/forgot-password")
public class ForgotPassword {

    private final OTPService otpService;
    private final UserService userService;
    private final EmailService emailService;

    public ForgotPassword(OTPService otpService, UserService userService, EmailService emailService) {
        this.otpService = otpService;
        this.userService = userService;
        this.emailService = emailService;
    }

    private boolean isEmailPatternNotValid(String email) {
        return !EmailValidator.getInstance().isValid(email);
    }

    @CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
    @RequestMapping(
            value = "/enter-email",
            method = RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<EnterEmailForForgotPasswordResponse> enterEmailForForgotPasswordRequest(@RequestBody final EnterEmailForForgotPasswordRequest body) {
        String email = body.getEmail();
        if (email == null || email.isEmpty() || isEmailPatternNotValid(email)) {
            return ResponseEntity.status(400).body(new EnterEmailForForgotPasswordResponse("Invalid email"));
        }
        email = email.toLowerCase();
        UserSchema user = userService.getUserByEmail(email);
        if (user == null) {
            return ResponseEntity.status(404).body(new EnterEmailForForgotPasswordResponse("User not found"));
        }
        if (user.getPassword() == null) {
            return ResponseEntity.status(400).body(new EnterEmailForForgotPasswordResponse("User has no password"));
        }
        if (user.getPassword().isEmpty()) {
            return ResponseEntity.status(400).body(new EnterEmailForForgotPasswordResponse("User has no password"));
        }
        OTPSchema existingOtp = otpService.getOTPByEmail(email);
        if (existingOtp != null) {
            return ResponseEntity.status(400).body(new EnterEmailForForgotPasswordResponse("OTP already sent"));
        }
        StringBuilder otpGenerated = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        try {
            secureRandom = SecureRandom.getInstance(secureRandom.getAlgorithm());
            otpGenerated.append(secureRandom.nextInt(9) + 1);
            for (int i = 0; i < 5; i++) {
                otpGenerated.append(secureRandom.nextInt(10));
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(new EnterEmailForForgotPasswordResponse("Internal server error"));
        }
        String otpString = otpGenerated.toString();
        OTPSchema otp = new OTPSchema(email, otpString);
        emailService.sendEmail(email, "ImageDB OTP", "Your OTP is " + otpString);
        otpService.createOTP(otp);
        return ResponseEntity.status(200).body(new EnterEmailForForgotPasswordResponse("OTP sent successfully"));
    }

    @CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
    @RequestMapping(
            value = "/resend-otp",
            method = RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<EnterEmailForForgotPasswordResponse> resendOTP(@RequestBody final EnterEmailForForgotPasswordRequest body) {
        String email = body.getEmail();
        if (email == null || email.isEmpty() || isEmailPatternNotValid(email)) {
            return ResponseEntity.status(400).body(new EnterEmailForForgotPasswordResponse("Invalid email"));
        }
        email = email.toLowerCase();
        UserSchema user = userService.getUserByEmail(email);
        if (user == null) {
            return ResponseEntity.status(404).body(new EnterEmailForForgotPasswordResponse("User not found"));
        }
        OTPSchema existingOtp = otpService.getOTPByEmail(email);
        if (existingOtp == null) {
            return ResponseEntity.status(404).body(new EnterEmailForForgotPasswordResponse("OTP not found"));
        }
        int count = existingOtp.getCount();
        if (count >= 3) {
            return ResponseEntity.status(400).body(new EnterEmailForForgotPasswordResponse("OTP limit exceeded"));
        }
        existingOtp.setCount(count + 1);
        emailService.sendEmail(email, "ImageDB OTP", "Your OTP is " + existingOtp.getOtp());
        otpService.updateOTP(existingOtp);
        return ResponseEntity.status(200).body(new EnterEmailForForgotPasswordResponse("OTP sent successfully"));
    }
}
