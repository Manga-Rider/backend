package com.mangarider.model.dto.response;

import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.time.Instant;
import java.util.Map;

@Builder
public record ErrorMessageResponse(
        Instant createdAt,
        HttpStatus statusCode,
        int status,
        String message,
        Map<String, Object> details
) {
}