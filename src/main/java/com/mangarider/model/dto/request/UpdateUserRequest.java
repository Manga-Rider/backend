package com.mangarider.model.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record UpdateUserRequest (
        String username,
        @JsonFormat(pattern = "dd-MM-yyyy")
        LocalDate birthday,
        String location
) {
}
