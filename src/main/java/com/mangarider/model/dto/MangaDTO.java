package com.mangarider.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mangarider.model.entity.Manga;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record MangaDTO(
        UUID mangaId,
        UUID author,
        String image,
        String title,
        String description,
        Manga.Status status,
        @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
        LocalDateTime publishedAt
) {
}
