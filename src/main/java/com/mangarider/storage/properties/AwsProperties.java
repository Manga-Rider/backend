package com.mangarider.storage.properties;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
public class AwsProperties {

    @Value("${aws.access.key}")
    private String accessKey;

    @Value("${aws.secret.key}")
    private String secretKey;

    @Value("${aws.s3.bucket}")
    private String s3Bucket;

    @Value("${aws.s3.region}")
    private String s3Region;
}