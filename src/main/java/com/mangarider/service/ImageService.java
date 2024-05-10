package com.mangarider.service;

import com.mangarider.exception.InvalidFileException;
import com.mangarider.model.entity.Image;
import com.mangarider.model.entity.Manga;
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

import java.util.ArrayList;
import java.util.List;
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
    public Pair<User, Image> setUserImage(User user, MultipartFile file) {
        validateFile(file);

        String key = generateS3Key(user.getUserId(), file.getOriginalFilename());
        if (user.getImage() != null) {
            service.deleteFile(user.getImage().getS3Key());
            user.getImage().setS3Key(key);
        } else {
            Image image = createAndSaveImage(key);
            user.setImage(image);
            userRepository.save(user);
        }
        Image image = user.getImage();
        upload(key, file);

        return Pair.with(user, image);
    }

    @Transactional
    public Pair<Manga, Image> setCoverImage(UUID userId, Manga manga, MultipartFile file) {
        validateFile(file);

        String key = generateS3Key(userId, manga.getMangaId(), file.getOriginalFilename());
        Image image = createAndSaveImage(key);
        manga.setCover(image);
        manga.addImage(image);
        upload(key, file);

        return Pair.with(manga, image);
    }

    @Transactional
    public Pair<Manga, List<Image>> addImages(UUID userId, Manga manga, MultipartFile[] files) {
        if (files.length < 1) {
            return Pair.with(manga, List.of());
        }

        List<Image> result = new ArrayList<>(files.length);
        for (var file : files) {
            validateFile(file);
            String key = generateS3Key(userId, manga.getMangaId(), file.getOriginalFilename());
            Image image = Image.builder()
                    .s3Key(key)
                    .build();
            manga.addImage(image);
            result.add(image);
            upload(key, file);
        }
        result = imageRepository.saveAll(result);

        return Pair.with(manga, result);
    }

    public List<String> getUrls(List<Image> images) {
        if (images == null || images.isEmpty()) {
            return List.of();
        }
        return images.stream().map(image -> service.getPublicUrl(image.getS3Key())).toList();
    }

    public String getUrl(Image image) {
        if (image == null) {
            return null;
        }

        return service.getPublicUrl(image.getS3Key());
    }

    private void upload(String key, MultipartFile file) {
        try {
            service.uploadFile(key, file.getInputStream());
        } catch (Exception e) {
            throw new InvalidFileException("File corrupted: " + e.getMessage());
        }
    }


    private void validateFile(MultipartFile file) {
        if (!StringUtils.hasLength(file.getOriginalFilename()) || !isValidFileName(file.getOriginalFilename())) {
            throw new InvalidFileException("Invalid file name");
        }
        if (!properties.isSupportedType(file)) {
            throw new InvalidFileException("File type {%s} is unsupported".formatted(file.getContentType()));
        }
    }

    private String generateS3Key(UUID userId, String fileName) {
        return properties.userFolder().formatted(userId, UUID.randomUUID() + fileName);
    }

    private String generateS3Key(UUID userId, UUID mangaId, String fileName) {
        return properties.mangaFolder().formatted(userId, mangaId, UUID.randomUUID() + fileName);
    }

    private Image createAndSaveImage(String key) {
        Image image = Image.builder().s3Key(key).build();
        return imageRepository.save(image);
    }

    private boolean isValidFileName(String name) {
        Pattern pattern = Pattern.compile(properties.regex());
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }
}
