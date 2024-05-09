package com.mangarider.model.dto.request;

import lombok.Builder;

@Builder
public record CreateMangaRequest (
        String title,
        String description
) {
}
