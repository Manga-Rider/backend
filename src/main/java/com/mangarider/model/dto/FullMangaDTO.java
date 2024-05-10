package com.mangarider.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mangarider.model.entity.Manga;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record FullMangaDTO (
        UUID mangaId,
        UUID author,
        String image,
        String title,
        String description,
        Manga.Status status,
        List<String> images,
        @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
        LocalDateTime publishedAt,
        @JsonFormat(pattern = "dd-MM-yyyy")
        LocalDate createdAt
) {
}
