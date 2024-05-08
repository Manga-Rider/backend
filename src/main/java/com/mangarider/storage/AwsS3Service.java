package com.mangarider.storage;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.mangarider.storage.properties.AwsProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.net.URL;

@Service
@RequiredArgsConstructor
public class AwsS3Service implements StorageService {
    private final AmazonS3 s3Client;
    private final AwsProperties properties;

    public void uploadFile(String key, InputStream inputStream) {
        if (key == null || inputStream == null) {
            return;
        }

        ObjectMetadata metadata = new ObjectMetadata();
        PutObjectRequest request = new PutObjectRequest(properties.getS3Bucket(), key, inputStream, metadata);
        s3Client.putObject(request);
    }

    public void deleteFile(String key) {
        if (key == null) {
            return;
        }

        s3Client.deleteObject(properties.getS3Bucket(), key);
    }

    public String getPublicUrl(String key) {
        if (key == null) {
            return null;
        }

        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(properties.getS3Bucket(), key);
        URL url = s3Client.generatePresignedUrl(request);
        return url.toString();
    }
}
