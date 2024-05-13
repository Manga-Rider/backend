package com.mangarider.model.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ChapterPageDTO(
        UUID pageId,
        long pageNumber
) {
}
