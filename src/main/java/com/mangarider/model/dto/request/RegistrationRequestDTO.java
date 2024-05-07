package com.mangarider.model.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record RegistrationRequestDTO(
        @Email
        @NotEmpty
        String email,
        @NotEmpty
        @Size(min = 8)
        String username,
        @Size(min = 8)
        @NotEmpty
        String password
) {
}
