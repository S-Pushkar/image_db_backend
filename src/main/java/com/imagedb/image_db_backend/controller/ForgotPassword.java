package com.imagedb.image_db_backend.controller;

import com.imagedb.image_db_backend.model.*;
import com.imagedb.image_db_backend.service.EmailService;
import com.imagedb.image_db_backend.service.OTPService;
import com.imagedb.image_db_backend.service.UserService;
import com.imagedb.image_db_backend.service.VerifiedUserService;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

@RestController
@RequestMapping("/api/forgot-password")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ForgotPassword {

    private final OTPService otpService;
    private final UserService userService;
    private final EmailService emailService;
    private final VerifiedUserService verifiedUserService;

    public ForgotPassword(OTPService otpService, UserService userService, EmailService emailService, VerifiedUserService verifiedUserService) {
        this.otpService = otpService;
        this.userService = userService;
        this.emailService = emailService;
        this.verifiedUserService = verifiedUserService;
    }

    private boolean isEmailPatternNotValid(String email) {
        return !EmailValidator.getInstance().isValid(email);
    }

    @RequestMapping(
            value = "/enter-email",
            method = RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<ForgotPasswordResponse> enterEmailForForgotPasswordRequest(@RequestBody final EnterEmailForForgotPasswordRequest body) {
        String email = body.getEmail();
        if (email == null || email.isEmpty() || isEmailPatternNotValid(email)) {
            return ResponseEntity.status(400).body(new ForgotPasswordResponse("Invalid email"));
        }
        email = email.toLowerCase();
        UserSchema user = userService.getUserByEmail(email);
        if (user == null) {
            return ResponseEntity.status(404).body(new ForgotPasswordResponse("User not found"));
        }
        if (user.getPassword() == null) {
            return ResponseEntity.status(400).body(new ForgotPasswordResponse("User has no password"));
        }
        if (user.getPassword().isEmpty()) {
            return ResponseEntity.status(400).body(new ForgotPasswordResponse("User has no password"));
        }
        VerifiedUserSchema existingVerifiedUser = verifiedUserService.getVerifiedUserByEmail(email);
        if (existingVerifiedUser != null) {
            return ResponseEntity.status(400).body(new ForgotPasswordResponse("OTP already verified"));
        }
        OTPSchema existingOtp = otpService.getOTPByEmail(email);
        if (existingOtp != null) {
            return ResponseEntity.status(400).body(new ForgotPasswordResponse("OTP already sent"));
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
            return ResponseEntity.status(500).body(new ForgotPasswordResponse("Internal server error"));
        }
        String otpString = otpGenerated.toString();
        OTPSchema otp = new OTPSchema(email, otpString);
        emailService.sendEmail(email, "ImageDB OTP", "Your OTP is " + otpString);
        otpService.createOTP(otp);
        return ResponseEntity.status(200).body(new ForgotPasswordResponse("OTP sent successfully"));
    }

    @RequestMapping(
            value = "/resend-otp",
            method = RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<ForgotPasswordResponse> resendOTP(@RequestBody final EnterEmailForForgotPasswordRequest body) {
        String email = body.getEmail();
        if (email == null || email.isEmpty() || isEmailPatternNotValid(email)) {
            return ResponseEntity.status(400).body(new ForgotPasswordResponse("Invalid email"));
        }
        email = email.toLowerCase();
        OTPSchema existingOtp = otpService.getOTPByEmail(email);
        if (existingOtp == null) {
            return ResponseEntity.status(404).body(new ForgotPasswordResponse("OTP not found"));
        }
        int count = existingOtp.getCount();
        if (count >= 3) {
            return ResponseEntity.status(400).body(new ForgotPasswordResponse("OTP limit exceeded"));
        }
        existingOtp.setCount(count + 1);
        emailService.sendEmail(email, "ImageDB OTP", "Your OTP is " + existingOtp.getOtp());
        otpService.updateOTP(existingOtp);
        return ResponseEntity.status(200).body(new ForgotPasswordResponse("OTP sent successfully"));
    }

    @RequestMapping(
            value = "/verify-otp",
            method = RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<ForgotPasswordResponse> verifyOTP(@RequestBody final VerifyOTPForForgotPasswordRequest body) {
        String email = body.getEmail();
        String otp = body.getOtp();
        if (email == null || email.isEmpty() || isEmailPatternNotValid(email)) {
            return ResponseEntity.status(400).body(new ForgotPasswordResponse("Invalid email"));
        }
        email = email.toLowerCase();
        OTPSchema existingOtp = otpService.getOTPByEmail(email);
        if (existingOtp == null) {
            return ResponseEntity.status(404).body(new ForgotPasswordResponse("OTP not found"));
        }
        if (!existingOtp.getOtp().equals(otp)) {
            return ResponseEntity.status(400).body(new ForgotPasswordResponse("Invalid OTP"));
        }
        otpService.deleteOTP(existingOtp);
        VerifiedUserSchema existingVerifiedUser = verifiedUserService.getVerifiedUserByEmail(email);
        if (existingVerifiedUser != null) {
            return ResponseEntity.status(400).body(new ForgotPasswordResponse("OTP already verified"));
        }
        verifiedUserService.saveVerifiedUser(email);
        return ResponseEntity.status(200).body(new ForgotPasswordResponse("OTP verified successfully"));
    }
}
