package com.mangarider.service;

import com.mangarider.exception.UserNotFoundException;
import com.mangarider.mapper.MangaMapper;
import com.mangarider.model.dto.ImageDTO;
import com.mangarider.model.dto.UserDTO;
import com.mangarider.model.entity.User;
import com.mangarider.repository.ImageRepository;
import com.mangarider.repository.UserCredentialsRepository;
import com.mangarider.repository.UserRepository;
import com.mangarider.storage.AwsS3Service;
import com.mangarider.storage.StorageService;
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
    private final ImageService imageService;
    private final MangaMapper mapper;

    public User getUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id = {%s} not found".formatted(userId)));
    }

    @Transactional
    public Page<UserDTO> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(user -> mapper.toDTO(user, imageService.getUrl(user.getImage())));
    }

    public Optional<UserDTO> findUser(UUID userId) {
        return userRepository.findById(userId).map(user ->
                mapper.toDTO(user, imageService.getUrl(user.getImage())));
    }


}
