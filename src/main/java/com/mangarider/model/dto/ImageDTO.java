package com.mangarider.model.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ImageDTO (
        UUID imageId,
        String url
) {
}
