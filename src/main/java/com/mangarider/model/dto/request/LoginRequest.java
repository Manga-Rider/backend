package com.mangarider.model.dto.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public record LoginRequest(
        @NotEmpty
        @Email
        String email,
        @NotEmpty
        String password
) {
}
