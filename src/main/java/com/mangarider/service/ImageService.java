package com.mangarider.service;

import com.mangarider.exception.InvalidFileException;
import com.mangarider.model.entity.Image;
import com.mangarider.model.entity.User;
import com.mangarider.repository.ImageRepository;
import com.mangarider.repository.UserRepository;
import com.mangarider.storage.StorageService;
import com.mangarider.storage.properties.ImageProperties;
import lombok.RequiredArgsConstructor;
import org.javatuples.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final StorageService service;
    private final ImageProperties properties;

    @Transactional
    public Pair<UUID, String> addUserImage(User user, MultipartFile file) {
        if (!StringUtils.hasLength(file.getOriginalFilename())) {
            throw new InvalidFileException("Invalid file name");
        }
        if (!properties.isSupportedType(file)) {
            throw new InvalidFileException("File type {%s} is unsupported".formatted(file.getContentType()));
        }
        String key = generateS3Key(user.getUserId(), file.getName());
        Image image = Image.builder()
                .s3Key(key)
                .build();
        user.getImages().add(image);
        userRepository.save(user);
        try {
            service.uploadFile(key, file.getInputStream());
        } catch (Exception e) {
            throw new InvalidFileException("File corrupted");
        }

        return Pair.with(image.getImageId(), image.getS3Key());
    }

    private String generateS3Key(UUID userId, String fileName) {
        return properties.userFolder().formatted(userId, fileName);
    }
}
