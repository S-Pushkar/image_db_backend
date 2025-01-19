package com.imagedb.image_db_backend.controller;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.imagedb.image_db_backend.model.TokenInput;
import com.imagedb.image_db_backend.model.UserSchema;
import com.imagedb.image_db_backend.service.UserService;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api")
public class FetchAllImages {

    private final Dotenv dotenv;
    private final UserService userService;
    private final BlobContainerClient containerClient;

    @Autowired
    public FetchAllImages(UserService userService, BlobContainerClient containerClient) {
        this.userService = userService;
        this.dotenv = Dotenv.configure().ignoreIfMissing().load();
        this.containerClient = containerClient;
    }

    @RequestMapping(
            value = "/fetch-all-images",
            method = RequestMethod.POST,
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<List<String>> fetchAllImages(@RequestBody final TokenInput tokenInput) {
        String token = tokenInput.getToken();

        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Claims claims;

        try {
            claims = Jwts.parser()
                    .setSigningKey(dotenv.get("JWT_SECRET"))
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException e) {
            return ResponseEntity.badRequest().build();
        }

        final String id = claims.get("id").toString();
        final String name = claims.get("name").toString();
        final String email = claims.get("email").toString();

        Optional<UserSchema> user = userService.getUserById(id);

        if (user.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        UserSchema userSchema = user.get();

        if (!userSchema.getEmail().equals(email) || !userSchema.getName().equals(name)) {
            return ResponseEntity.badRequest().build();
        }

        String fileNamePrefix = email + "-";
        List<String> imageBase64s = new ArrayList<>();

        try {
            this.containerClient.listBlobsByHierarchy(fileNamePrefix).forEach(blobItem -> {
                String blobName = blobItem.getName();
                BlobClient blobClient = this.containerClient.getBlobClient(blobName);
                try (InputStream inputStream = blobClient.openInputStream()) {
                    byte[] bytes = inputStream.readAllBytes();
                    String base64 = Base64.getEncoder().encodeToString(bytes);
                    imageBase64s.add(base64);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok(imageBase64s);
    }
}
