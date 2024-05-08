package com.mangarider.service;

import com.mangarider.exception.UserNotFoundException;
import com.mangarider.model.dto.ImageDTO;
import com.mangarider.model.entity.User;
import com.mangarider.repository.ImageRepository;
import com.mangarider.repository.UserCredentialsRepository;
import com.mangarider.repository.UserRepository;
import com.mangarider.storage.AwsS3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserCredentialsRepository credentialsRepository;
    private final ImageRepository imageRepository;
    private final AwsS3Service awsS3Service;

    public User getUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id = {%s} not found".formatted(userId)));
    }

    public Page<User> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    public Optional<User> findUser(UUID userId) {
        return userRepository.findById(userId);
    }

    @Transactional
    public Page<ImageDTO> getUserImages(UUID userId, Pageable pageable) {
        User user = getUser(userId);
        List<ImageDTO> images = user.getImages().stream()
                .map(image -> new ImageDTO(image.getImageId(), awsS3Service.getPublicUrl(image.getS3Key())))
                .toList();
        return new PageImpl<>(images, pageable, images.size());
    }


}
