package com.mangarider.mapper;

import com.mangarider.model.dto.*;
import com.mangarider.model.entity.Chapter;
import com.mangarider.model.entity.ChapterPage;
import com.mangarider.model.entity.Manga;
import com.mangarider.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

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

    public FullMangaDTO toFullDTO(Manga manga, String image, List<String> images) {
        if (manga == null) {
            return null;
        }

        return FullMangaDTO.builder()
                .mangaId(manga.getMangaId())
                .author(manga.getAuthor().getUserId())
                .status(manga.getStatus())
                .title(manga.getTitle())
                .image(image)
                .images(images)
                .description(manga.getDescription())
                .publishedAt(manga.getPublishedAt())
                .createdAt(manga.getCreatedAt().toLocalDate())
                .status(manga.getStatus())
                .build();
    }

    public ChapterDTO toDTO(Chapter chapter, long pages) {
        if (chapter == null) {
            return null;
        }

        return ChapterDTO.builder()
                .mangaId(chapter.getChapterId())
                .title(chapter.getTitle())
                .pages(pages)
                .releaseDate(chapter.getReleaseDate())
                .build();
    }

    public ChapterPageDTO toDTO(ChapterPage page) {
        if (page == null) {
            return null;
        }

        return ChapterPageDTO.builder()
                .pageId(page.getPageId())
                .pageNumber(page.getPageNumber())
                .build();
    }

    public FullChapterPageDTO toFullDTO(ChapterPage page, String image) {
        if (page == null) {
            return null;
        }

        return FullChapterPageDTO.builder()
                .pageId(page.getPageId())
                .pageNumber(page.getPageNumber())
                .image(image)
                .build();
    }
}
