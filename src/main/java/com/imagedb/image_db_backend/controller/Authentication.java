package com.imagedb.image_db_backend.controller;

import com.imagedb.image_db_backend.model.*;
import com.imagedb.image_db_backend.service.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.regex.Pattern;

@RestController
@RequestMapping("/api/auth")
public class Authentication {

    private final UserService userService;

    @Autowired
    public Authentication(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "/hello", method = RequestMethod.GET, produces = "text/plain")
    public String hello() {
        return "Hello, World!";
    }

    private boolean checkEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);
        return pat.matcher(email).matches();
    }

    @RequestMapping(
            value = "/sign-in",
            method = RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<SignInAndSignUpResponse> login(@RequestBody final SignInDetails signInDetails) {
        String email = signInDetails.getEmail();
        String password = signInDetails.getPassword();
        if (email == null || password == null || email.isEmpty() || password.isEmpty() || !checkEmail(email)) {
            return ResponseEntity.status(400).build();
        }
        email = email.toLowerCase();
        if (userService.getUserByEmail(email) == null) {
            return ResponseEntity.status(404).build();
        }
        if (userService.checkPassword(email, password)) {
            String token = Jwts.builder().setSubject(email).signWith(SignatureAlgorithm.HS512, "secret").compact();
            return ResponseEntity.ok(new SignInAndSignUpResponse(token));
        } else {
            return ResponseEntity.status(401).build();
        }
    }

    @RequestMapping(
            value = "/sign-up",
            method = RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<SignInAndSignUpResponse> register(@RequestBody final SignUpDetails signUpDetails) {
        String email = signUpDetails.getEmail();
        String name = signUpDetails.getName();
        String password = signUpDetails.getPassword();
        if (email == null || name == null || password == null || email.isEmpty() || name.isEmpty() || password.isEmpty() || !checkEmail(email)) {
            return ResponseEntity.status(400).build();
        }
        email = email.toLowerCase();
        if (userService.getUserByEmail(email) != null) {
            return ResponseEntity.status(409).build();
        }
        UserSchema newUser = new UserSchema(name, email, password);
        userService.createUser(newUser);
        String token = Jwts.builder().setSubject(email).signWith(SignatureAlgorithm.HS512, "secret").compact();
        return ResponseEntity.ok(new SignInAndSignUpResponse(token));
    }

    @RequestMapping(
            value = "/nextauth-sign-in",
            method = RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<Boolean> nextAuthLogin(@RequestBody final NextAuthSignInDetails nextAuthSignInDetails) {
        String name = nextAuthSignInDetails.getName();
        String email = nextAuthSignInDetails.getEmail();
        if (email == null || email.isEmpty() || !checkEmail(email)) {
            return ResponseEntity.status(400).body(false);
        }
        email = email.toLowerCase();
        if (name == null || name.isEmpty()) {
            name = email.split("@")[0];
        }
        UserSchema existingUser = userService.getUserByEmail(email);
        if (existingUser == null) {
            UserSchema newUser = new UserSchema(name, email, null);
            userService.createUser(newUser);
        } else {
            if (existingUser.getPassword() == null ||  !existingUser.getPassword().isEmpty()) {
                return ResponseEntity.status(409).body(false);
            }
            if (!existingUser.getName().equals(name)) {
                existingUser.setName(name);
                userService.updateUser(existingUser.getId(), existingUser);
            }
        }
        return ResponseEntity.ok(true);
    }
}
