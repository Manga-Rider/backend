package com.mangarider.service;

import com.mangarider.exception.UserNotFoundException;
import com.mangarider.mapper.MangaMapper;
import com.mangarider.model.dto.UserDTO;
import com.mangarider.model.dto.request.UpdateUserRequest;
import com.mangarider.model.entity.User;
import com.mangarider.model.entity.UserCredentials;
import com.mangarider.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ImageService imageService;
    private final MangaMapper mapper;

    public UserDTO getUser(UUID userId) {
        return userRepository.findById(userId)
                .map(user -> mapper.toDTO(user, imageService.getUrl(user.getImage())))
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

    @Transactional
    public void update(UserCredentials credentials, UpdateUserRequest request) {
        if (credentials == null || credentials.getUser() == null) {
            return;
        }
        User user = credentials.getUser();
        if (request.username() != null) {
            user.setUsername(request.username());
        }
        if (request.birthday() != null) {
            user.setBirthday(request.birthday());
        }
        if (request.location() != null) {
            user.setLocation(request.location());
        }

        userRepository.save(user);
    }

}
