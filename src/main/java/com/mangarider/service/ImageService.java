package com.mangarider.service;

import com.mangarider.exception.InvalidFileException;
import com.mangarider.model.entity.UserImage;
import com.mangarider.model.entity.User;
import com.mangarider.repository.ImageRepository;
import com.mangarider.repository.UserRepository;
import com.mangarider.storage.StorageService;
import com.mangarider.storage.properties.ImageProperties;
import lombok.RequiredArgsConstructor;
import org.javatuples.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class ImageService {
    private final UserRepository userRepository;
    private final ImageRepository imageRepository;
    private final StorageService service;
    private final ImageProperties properties;

    @Transactional
    public Pair<UUID, String> setUserImage(User user, MultipartFile file) {
        if (!StringUtils.hasLength(file.getOriginalFilename()) || !validName(file.getOriginalFilename())) {
            throw new InvalidFileException("Invalid file name");
        }
        if (!properties.isSupportedType(file)) {
            throw new InvalidFileException("File type {%s} is unsupported".formatted(file.getContentType()));
        }
        String key = generateS3Key(user.getUserId(), file.getOriginalFilename());
        if (user.getImage() != null) {
            service.deleteFile(user.getImage().getS3Key());
            user.getImage().setS3Key(key);
        } else {
            UserImage image = UserImage.builder()
                    .user(user)
                    .s3Key(key)
                    .build();
            image = imageRepository.save(image);
            user.setImage(image);
        }
        UserImage image = user.getImage();
        try {
            service.uploadFile(key, file.getInputStream());
        } catch (Exception e) {
            throw new InvalidFileException("File corrupted");
        }

        return Pair.with(image.getImageId(), image.getS3Key());
    }

    public String getUrl(UserImage image) {
        if (image == null) {
            return null;
        }

        return service.getPublicUrl(image.getS3Key());
    }

    private String generateS3Key(UUID userId, String fileName) {
        return properties.userFolder().formatted(userId, fileName);
    }

    private boolean validName(String name) {
        Pattern pattern = Pattern.compile(properties.regex());
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }
}
