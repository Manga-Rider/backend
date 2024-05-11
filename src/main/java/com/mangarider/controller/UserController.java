package com.mangarider.controller;

import com.mangarider.model.dto.UserDTO;
import com.mangarider.model.dto.request.UpdateUserRequest;
import com.mangarider.model.entity.UserCredentials;
import com.mangarider.service.ImageService;
import com.mangarider.service.UserService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ImageService imageService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUser(@PathVariable("userId") UUID userId) {
        return ResponseEntity.of(userService.findUser(userId));
    }

    @GetMapping()
    private ResponseEntity<Page<UserDTO>> getUsers(
            @RequestParam(value = "size", defaultValue = "20")
            @Min(1) @Max(1000) int size,
            @RequestParam(value = "num", defaultValue = "0")
            @PositiveOrZero int num,
            @RequestParam(value = "orderBy", defaultValue = "DESC")
            @Pattern(
                    regexp = "asc|desc",
                    flags = {Pattern.Flag.CASE_INSENSITIVE},
                    message = "error.validation.sort.direction.message"
            )
            String order,
            @RequestParam(value = "properties", defaultValue = "createdAt")
            String[] properties
    ) {
        return ResponseEntity.ok(
                userService.getUsers(PageRequest.of(num, size, Sort.Direction.fromString(order), properties))
        );
    }

    @GetMapping("/personal/account")
    public UserDTO getPersonalAccount(@AuthenticationPrincipal UserCredentials user) {
        return userService.getUser(user.getUserId());
    }

    @PutMapping("/personal/account")
    public void update(@AuthenticationPrincipal UserCredentials credentials,
                       @RequestBody UpdateUserRequest request
    ) {
        userService.update(credentials, request);
    }

    @PostMapping(path = "/personal/account/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void addImage(@AuthenticationPrincipal UserCredentials user,
                                      @RequestParam("file") MultipartFile file) {
        imageService.setUserImage(user.getUser(), file);
    }
}
