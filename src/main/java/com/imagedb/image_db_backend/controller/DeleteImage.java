package com.imagedb.image_db_backend.controller;

import com.azure.storage.blob.BlobContainerClient;
import com.imagedb.image_db_backend.model.RequestForDeleteImage;
import com.imagedb.image_db_backend.model.UserSchema;
import com.imagedb.image_db_backend.service.DeleteFileProducerService;
import com.imagedb.image_db_backend.service.UserService;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api")
public class DeleteImage {

    private final Dotenv dotenv;
    private final UserService userService;
    private final BlobContainerClient containerClient;
    private final DeleteFileProducerService deleteFileProducerService;

    @Autowired
    public DeleteImage(UserService userService, BlobContainerClient containerClient, DeleteFileProducerService deleteFileProducerService) {
        this.userService = userService;
        this.deleteFileProducerService = deleteFileProducerService;
        this.dotenv = Dotenv.configure().ignoreIfMissing().load();
        this.containerClient = containerClient;
    }

    @RequestMapping(
            value = "/delete-image",
            method = RequestMethod.DELETE,
            consumes = "application/json",
            produces = "application/json")
    public ResponseEntity<String> deleteImage(@RequestBody final RequestForDeleteImage requestForDeleteImage) {
        String token = requestForDeleteImage.getToken();
        String imageName = requestForDeleteImage.getImageName();

        if (token == null || token.isEmpty() || imageName == null || imageName.isEmpty()) {
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

        this.deleteFileProducerService.sendMessage("delete-file", email, imageName);

        this.containerClient.getBlobClient(imageName).deleteIfExists();

        return ResponseEntity.ok("Image deleted");
    }
}
