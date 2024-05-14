package com.mangarider.test.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record RegistrationRequestTest(
        String email,
        String username,
        String password,
        LocalDate birthday,
        String location
) {
}
