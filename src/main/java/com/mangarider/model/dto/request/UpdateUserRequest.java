package com.mangarider.model.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record UpdateUserRequest (
        String username,
        @JsonFormat(pattern = "DD-MM-YYYY")
        LocalDate birthday,
        String location
) {
}
