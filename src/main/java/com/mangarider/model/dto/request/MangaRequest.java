package com.mangarider.model.dto.request;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record MangaRequest(
        @NotEmpty
        @Size(min = 5)
        String title,
        @NotEmpty
        String description
) {
}
