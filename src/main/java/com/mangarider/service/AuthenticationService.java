package com.mangarider.service;

import com.mangarider.exception.AuthenticationException;
import com.mangarider.exception.UserAlreadyExistsException;
import com.mangarider.model.dto.request.Registration;
import com.mangarider.model.entity.User;
import com.mangarider.model.entity.UserCredentials;
import com.mangarider.model.entity.UserRole;
import com.mangarider.repository.UserCredentialsRepository;
import com.mangarider.repository.UserRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.javatuples.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@RequiredArgsConstructor
@Service
@Validated
public class AuthenticationService {
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final UserCredentialsRepository credentialsRepository;
    private final PasswordEncoder encoder;


    @Transactional
    public Pair<User, UserCredentials> registration(@NotNull Registration dto) {
        if (userRepository.existsByUsername(dto.username())) {
            throw new UserAlreadyExistsException("User with username = {%s} already exists".formatted(dto.username()));
        } else if (credentialsRepository.existsByEmail(dto.email())) {
            throw new UserAlreadyExistsException("User with email = {%s} already exists".formatted(dto.email()));
        }
        UserCredentials credentials = UserCredentials.builder()
                .email(dto.email())
                .password(encoder.encode(dto.password()))
                .roles(List.of(UserRole.USER))
                .build();
        User user = User.builder()
                .credentials(credentials)
                .username(dto.username())
                .birthday(dto.birthday())
                .location(dto.location())
                .build();
        credentials.setUser(user);
        user = userRepository.save(user);

        return Pair.with(user, credentials);
    }

    public String login(@NotNull String email, @NotNull String password) {
        UserCredentials credentials = credentialsRepository.findByEmail(email)
                .orElseThrow(() -> new AuthenticationException(HttpStatus.UNAUTHORIZED, "Invalid username or password"));
        if (!encoder.matches(password, credentials.getPassword())) {
            throw new AuthenticationException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }
        return jwtService.generateToken(credentials);
    }

    public boolean isUserExists(String username, String email) {
        return userRepository.existsByUsername(username) || credentialsRepository.existsByEmail(email);
    }
}
