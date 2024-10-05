package com.imagedb.image_db_backend.controller;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobErrorCode;
import com.azure.storage.blob.models.BlobStorageException;
import com.imagedb.image_db_backend.model.OutputForUploadFile;
import com.imagedb.image_db_backend.model.UserSchema;
import com.imagedb.image_db_backend.service.UploadFileProducerService;
import com.imagedb.image_db_backend.service.UserService;
import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/gateway")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class Gateway {

    private final Dotenv dotenv;
    private final UserService userService;
    private BlobContainerClient containerClient;
    private final UploadFileProducerService uploadFileProducerService;

    @Autowired
    public Gateway(UserService userService, UploadFileProducerService uploadFileProducerService) {
        this.userService = userService;
        this.dotenv = Dotenv.configure().ignoreIfMissing().load();
        this.uploadFileProducerService = uploadFileProducerService;
        String endpoint = dotenv.get("AZURE_STORAGE_ACCOUNT_URL");
        String sasToken = dotenv.get("AZURE_STORAGE_ACCOUNT_SAS");

        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .endpoint(endpoint)
                .sasToken(sasToken)
                .buildClient();

        try {
            this.containerClient = blobServiceClient.createBlobContainer("images");
        } catch (BlobStorageException ex) {
            if (!ex.getErrorCode().equals(BlobErrorCode.CONTAINER_ALREADY_EXISTS)) {
                throw ex;
            }
            this.containerClient = blobServiceClient.getBlobContainerClient("images");
        }
    }

    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /*
        * Example curl request:
        * curl -X POST http://localhost:8080/api/gateway/upload-image \
    -H "Content-Type: multipart/form-data" \
    -F "image=@/path" \
    -F "token="hjhhj.cwdb.cwbd"
     */
    @RequestMapping(
            value = "/upload-image",
            method = RequestMethod.POST,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = "application/json")
    public ResponseEntity<OutputForUploadFile> uploadImage(@RequestParam("token") String token, @RequestParam("image") MultipartFile image) throws IOException {
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(401).body(new OutputForUploadFile("No token provided"));
        }
        if (image == null || image.isEmpty()) {
            return ResponseEntity.status(400).body(new OutputForUploadFile("No image provided"));
        }

        Claims claims;

        try {
            claims = Jwts.parser()
                    .setSigningKey(dotenv.get("JWT_SECRET"))
                    .parseClaimsJws(token)
                    .getBody();
        } catch (JwtException ex) {
            return ResponseEntity.status(401).body(new OutputForUploadFile("Invalid token"));
        }

        final String id = claims.get("id").toString();
        final String name = claims.get("name").toString();
        final String email = claims.get("email").toString();

        Optional<UserSchema> user = userService.getUserById(id);

        if (user.isEmpty()) {
            return ResponseEntity.status(401).body(new OutputForUploadFile("User not found"));
        }

        UserSchema userSchema = user.get();
        int numberOfImages = userSchema.getNumberOfImages();

        if (!userSchema.getName().equals(name) || !userSchema.getEmail().equals(email)) {
            return ResponseEntity.status(401).body(new OutputForUploadFile("Invalid token"));
        }

        if (this.containerClient == null) {
            return ResponseEntity.status(500).body(new OutputForUploadFile("Error creating container"));
        }

        numberOfImages++;

        final String fileName = email
                + "-"
                + numberOfImages
                + getFileExtension(Objects.requireNonNull(image.getOriginalFilename()));

        uploadFileProducerService.sendMessage("upload-file", email, fileName, image.getBytes());

        try {
            BlobClient blobClient = this.containerClient.getBlobClient(fileName);
            blobClient.upload(image.getResource().getInputStream(), image.getSize());
        } catch (BlobStorageException ex) {
            return ResponseEntity.status(500).body(new OutputForUploadFile("Error uploading image: " + ex.getMessage()));
        }

        userService.updateNumberOfImages(id, numberOfImages);

        return ResponseEntity.status(200).body(new OutputForUploadFile("Image uploaded successfully"));
    }
}
