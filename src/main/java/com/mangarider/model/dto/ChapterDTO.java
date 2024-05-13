package com.mangarider.model.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.util.UUID;

@Builder
public record ChapterDTO(
        UUID chapterId,
        UUID mangaId,
        String title,
        long pages,
        LocalDate releaseDate
) {
}
