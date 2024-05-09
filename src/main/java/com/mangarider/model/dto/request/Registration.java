package com.mangarider.model.dto.request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record Registration(
        @Email
        @NotEmpty
        String email,
        @NotEmpty
        @Size(min = 8)
        String username,
        @Size(min = 8)
        @NotEmpty
        String password,
        @JsonFormat(pattern = "DD-MM-YYYY")
        LocalDate birthday,
        String location
) {
}
