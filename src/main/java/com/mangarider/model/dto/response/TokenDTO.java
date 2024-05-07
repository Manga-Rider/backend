package com.mangarider.model.dto.response;

import lombok.Builder;

@Builder
public record TokenDTO(
        String token
) {
}
