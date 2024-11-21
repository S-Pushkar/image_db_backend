package com.imagedb.image_db_backend.config;

import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import com.azure.storage.blob.models.BlobErrorCode;
import com.azure.storage.blob.models.BlobStorageException;
import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BlobContainerClientFactory {

    private final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();

    @Bean
    BlobContainerClient blobContainerClient() {
        String endpoint = dotenv.get("AZURE_STORAGE_ACCOUNT_URL");
        String sasToken = dotenv.get("AZURE_STORAGE_ACCOUNT_SAS");

        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder()
                .endpoint(endpoint)
                .sasToken(sasToken)
                .buildClient();

        BlobContainerClient containerClient;

        try {
            containerClient = blobServiceClient.createBlobContainer("images");
        } catch (BlobStorageException ex) {
            if (!ex.getErrorCode().equals(BlobErrorCode.CONTAINER_ALREADY_EXISTS)) {
                throw ex;
            }
            containerClient = blobServiceClient.getBlobContainerClient("images");
        }

        return containerClient;
    }

}
