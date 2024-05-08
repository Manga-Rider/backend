package com.mangarider.storage.properties;

import jakarta.activation.MimeType;
import org.apache.http.entity.ContentType;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@ConfigurationProperties(prefix = "image")
public record ImageProperties(
        List<String> supportedMimeTypes,
        String userFolder,
        String mangaFolder
) {
    public ImageProperties {

    }

    public boolean isSupportedType(MultipartFile file) {
        return supportedMimeTypes.contains(file.getContentType());
    }
}
