package com.mangarider.controller;

import com.mangarider.mapper.MangaMapper;
import com.mangarider.model.dto.UserDTO;
import com.mangarider.model.entity.User;
import com.mangarider.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final MangaMapper mapper;
    private final UserService service;


    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUser(@PathVariable("userId")UUID userId) {
        return ResponseEntity.of(service.findUser(userId).map(mapper::toDTO));
    }

//    @GetMapping("/{userId}/images")
//    private ResponseEntity<Page<String>> getUserImages(@PathVariable("userId") UUID userId) {
//        return ResponseEntity.of(service)
//    }
}
