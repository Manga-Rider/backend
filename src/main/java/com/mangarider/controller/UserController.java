package com.mangarider.controller;

import com.mangarider.model.dto.request.RegistrationRequestDTO;
import com.mangarider.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final AuthenticationService authService;

    @PostMapping("/registration")
    public ResponseEntity<?> create(@RequestBody @Valid RegistrationRequestDTO request) {
        authService.registration(request.username(), request.email(), request.password());
        return ResponseEntity.ok().build();
    }
}
