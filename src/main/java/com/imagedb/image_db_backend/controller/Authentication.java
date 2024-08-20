package com.imagedb.image_db_backend.controller;

import com.imagedb.image_db_backend.model.*;
import com.imagedb.image_db_backend.service.UserService;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.validator.routines.EmailValidator;

@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
@RestController
@RequestMapping("/api/auth")
public class Authentication {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final Dotenv dotenv;

    @Autowired
    public Authentication(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.dotenv = Dotenv.configure().ignoreIfMissing().load();
    }

    @CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
    @RequestMapping(value = "/hello",
            method = RequestMethod.GET,
            produces = "text/plain")
    public String hello() {
        return "Hello, World!";
    }

    private boolean isEmailPatternNotValid(String email) {
        return !EmailValidator.getInstance().isValid(email);
    }

    @CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
    @RequestMapping(
            value = "/sign-in",
            method = RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<SignInAndSignUpResponse> login(@RequestBody final SignInDetails signInDetails) {
        String email = signInDetails.getEmail();
        String password = signInDetails.getPassword();
        if (email == null || email.isEmpty() || isEmailPatternNotValid(email)) {
            return ResponseEntity.status(400).body(new SignInAndSignUpResponse("", "Invalid email", "", ""));
        }
        if (password == null || password.isEmpty()) {
            return ResponseEntity.status(400).body(new SignInAndSignUpResponse("", "Invalid password", "", ""));
        }
        email = email.toLowerCase();
        UserSchema user = userService.getUserByEmail(email);
        if (user == null) {
            return ResponseEntity.status(404).body(new SignInAndSignUpResponse("", "User not found", "", ""));
        }
        String userName = user.getName();
        String userEmail = user.getEmail();
        if (passwordEncoder.matches(password, user.getPassword())) {
            String token = Jwts.builder()
                    .claim("id", user.getId())
                    .claim("name", userName)
                    .claim("email", userEmail)
                    .signWith(SignatureAlgorithm.HS512, dotenv.get("JWT_SECRET"))
                    .compact();
            return ResponseEntity.ok(new SignInAndSignUpResponse(token, "Success", userName, userEmail));
        } else {
            return ResponseEntity.status(401).body(new SignInAndSignUpResponse("", "Invalid password", "", ""));
        }
    }

    @CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
    @RequestMapping(
            value = "/sign-up",
            method = RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<SignInAndSignUpResponse> register(@RequestBody final SignUpDetails signUpDetails) {
        String email = signUpDetails.getEmail();
        String name = signUpDetails.getName();
        String password = signUpDetails.getPassword();
        if (email == null || email.isEmpty() || isEmailPatternNotValid(email)) {
            return ResponseEntity.status(400).body(new SignInAndSignUpResponse("", "Invalid email", "", ""));
        }
        if (name == null || name.isEmpty()) {
            return ResponseEntity.status(400).body(new SignInAndSignUpResponse("", "Invalid name", "", ""));
        }
        if (password == null || password.isEmpty()) {
            return ResponseEntity.status(400).body(new SignInAndSignUpResponse("", "Invalid password", "", ""));
        }
        email = email.toLowerCase();
        if (userService.getUserByEmail(email) != null) {
            return ResponseEntity.status(409).body(new SignInAndSignUpResponse("", "User already exists", "", ""));
        }
        UserSchema newUser = new UserSchema(name, email, password);
        userService.createUser(newUser);
        String token = Jwts.builder()
                .claim("id", newUser.getId())
                .claim("name", name)
                .claim("email", email)
                .signWith(SignatureAlgorithm.HS512, dotenv.get("JWT_SECRET"))
                .compact();
        return ResponseEntity.ok(new SignInAndSignUpResponse(token, "Success", name, email));
    }

    @CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*")
    @RequestMapping(
            value = "/nextauth-sign-in",
            method = RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<Boolean> nextAuthLogin(@RequestBody final NextAuthSignInDetails nextAuthSignInDetails) {
        String name = nextAuthSignInDetails.getName();
        String email = nextAuthSignInDetails.getEmail();
        if (email == null || email.isEmpty() || isEmailPatternNotValid(email)) {
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
            if (existingUser.getPassword() == null) {
                return ResponseEntity.status(409).body(false);
            }
            if (!existingUser.getPassword().isEmpty()) {
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
