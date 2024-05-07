package com.mangarider.model.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record UserDTO(
        UUID userId,
        String username,
        String image,
        LocalDate birthday,

        String location,
        long views,
        LocalDateTime createdAt
) {
}
