package com.mangarider.model.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record FullChapterPageDTO(
        UUID pageId,
        String image,
        long pageNumber

) {
}
