package com.mangarider.controller;

import com.mangarider.model.dto.request.LoginRequestDTO;
import com.mangarider.model.dto.request.RegistrationRequestDTO;
import com.mangarider.model.dto.response.TokenDTO;
import com.mangarider.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.CREATED;

@Validated
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authService;

    @PostMapping("/registration")
    public ResponseEntity<?> create(@RequestBody @Valid RegistrationRequestDTO request) {
        authService.registration(request.username(), request.email(), request.password());
        return ResponseEntity.status(CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO request) {
        String token = authService.login(request.email(), request.password());
        return ResponseEntity.ok().body(new TokenDTO(token));
    }
}
