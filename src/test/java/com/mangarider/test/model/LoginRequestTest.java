package com.mangarider.test.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

@Builder
public record LoginRequestTest(
        String email,
        String password
) {
}
