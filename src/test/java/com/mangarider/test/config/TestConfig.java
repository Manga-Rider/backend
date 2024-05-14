package com.mangarider.test.config;

import com.mangarider.storage.StorageService;
import com.mangarider.storage.properties.AwsProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.io.InputStream;

@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public StorageService storageServiceTest() {
        return new StorageService() {
            @Override
            public void uploadFile(String key, InputStream inputStream) {

            }

            @Override
            public void deleteFile(String key) {

            }

            @Override
            public String getPublicUrl(String key) {
                return "MOCKED S3 URL";
            }
        };
    }

}
