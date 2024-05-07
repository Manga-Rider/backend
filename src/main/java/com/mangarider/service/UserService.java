package com.mangarider.service;

import com.mangarider.exception.UserNotFoundException;
import com.mangarider.model.entity.User;
import com.mangarider.repository.ImageRepository;
import com.mangarider.repository.UserCredentialsRepository;
import com.mangarider.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserCredentialsRepository credentialsRepository;
    private final ImageRepository imageRepository;

    public User getUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User with id = {%s} not found".formatted(userId)));
    }

    public Optional<User> findUser(UUID userId) {
        return userRepository.findById(userId);
    }

//    public Page<String> getUserImages(Pageable pageable) {
//        return
//    }

    public Page<User> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }
}
