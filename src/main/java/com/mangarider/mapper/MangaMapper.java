package com.mangarider.mapper;

import com.mangarider.model.dto.FullMangaDTO;
import com.mangarider.model.dto.MangaDTO;
import com.mangarider.model.dto.UserDTO;
import com.mangarider.model.entity.Manga;
import com.mangarider.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
public class MangaMapper {

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public UserDTO toDTO(User user, String image) {
        if (user == null) {
            return null;
        }

        return UserDTO.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .image(image)
                .birthday(user.getBirthday())
                .views(user.getViews())
                .location(user.getLocation())
                .build();
    }

    public MangaDTO toDTO(Manga manga, String image) {
        if (manga == null) {
            return null;
        }

        return MangaDTO.builder()
                .mangaId(manga.getMangaId())
                .image(image)
                .title(manga.getTitle())
                .author(manga.getAuthor().getUserId())
                .description(manga.getDescription())
                .status(manga.getStatus())
                .build();
    }

    public FullMangaDTO toFullDTO(Manga manga, String image, List<String> images) {
        if (manga == null) {
            return null;
        }

        return FullMangaDTO.builder()
                .mangaId(manga.getMangaId())
                .author(manga.getAuthor().getUserId())
                .status(manga.getStatus())
                .image(image)
                .images(images)
                .description(manga.getDescription())
                .publishedAt(manga.getPublishedAt())
                .createdAt(manga.getCreatedAt().toLocalDate())
                .status(manga.getStatus())
                .build();
    }
}
