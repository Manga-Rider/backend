package com.mangarider.mapper;

import com.mangarider.model.dto.MangaDTO;
import com.mangarider.model.dto.UserDTO;
import com.mangarider.model.entity.Manga;
import com.mangarider.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MangaMapper {

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
}
