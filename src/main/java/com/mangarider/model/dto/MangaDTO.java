package com.mangarider.model.dto;

import com.mangarider.model.entity.Manga;
import lombok.Builder;

import java.util.UUID;

@Builder
public record MangaDTO(
        UUID mangaId,
        UUID author,
        String image,
        String title,
        String description,
        Manga.Status status
) {
}
