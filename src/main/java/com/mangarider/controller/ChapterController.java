package com.mangarider.controller;

import com.mangarider.model.dto.ChapterDTO;
import com.mangarider.model.dto.ChapterPageDTO;
import com.mangarider.model.dto.FullChapterPageDTO;
import com.mangarider.model.dto.request.ChapterCreationRequest;
import com.mangarider.model.dto.request.ChapterEditRequest;
import com.mangarider.model.entity.UserCredentials;
import com.mangarider.service.ChapterService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Validated
@RestController
@RequestMapping("/api/v1/mangas")
@RequiredArgsConstructor
public class ChapterController {
    private final ChapterService service;

    @GetMapping("/{mangaId}/chapters")
    public Page<ChapterDTO> getChapters(
            @PathVariable("mangaId") UUID mangaId,
            @AuthenticationPrincipal UserCredentials credentials,
            @RequestParam(value = "size", defaultValue = "20")
            @Min(1) @Max(1000) int size,
            @RequestParam(value = "num", defaultValue = "0")
            @PositiveOrZero int num,
            @RequestParam(value = "orderBy", defaultValue = "DESC")
            @Pattern(
                    regexp = "asc|desc",
                    flags = {Pattern.Flag.CASE_INSENSITIVE},
                    message = "Direction can be only ASC or DESC"
            )
            String order,
            @RequestParam(value = "properties", defaultValue = "releaseDate")
            String[] properties
    ) {
        return service.getChapters(
                mangaId,
                credentials,
                PageRequest.of(num, size, Sort.Direction.fromString(order), properties)
        );
    }

    @PostMapping("/{mangaId}/chapters")
    public ChapterDTO createChapter(
            @PathVariable("mangaId") UUID mangaId,
            @AuthenticationPrincipal UserCredentials credentials,
            @Valid @RequestBody ChapterCreationRequest chapter
    ) {
        return service.createChapter(mangaId, credentials, chapter);
    }

    @PutMapping("/{mangaId}/chapters/{chapterId}")
    public ChapterDTO editChapter(
            @AuthenticationPrincipal UserCredentials credentials,
            @PathVariable("mangaId") UUID mangaId,
            @PathVariable("chapterId") UUID chapterId,
            @Valid @RequestBody ChapterEditRequest request
    ) {
        return service.editChapter(credentials, mangaId, chapterId, request);
    }

    @DeleteMapping("/{mangaId}/chapters/{chapterId}")
    public void deleteChapter(
            @AuthenticationPrincipal UserCredentials credentials,
            @PathVariable("mangaId") UUID chapterId,
            @PathVariable("chapterId") UUID mangaId
    ) {
        service.deleteChapter(credentials, mangaId, chapterId);
    }

    @PostMapping("/{mangaId}/chapters/{chapterId}")
    public ChapterPageDTO createPage(
            @AuthenticationPrincipal UserCredentials credentials,
            @PathVariable("mangaId") UUID chapterId,
            @PathVariable("chapterId") UUID mangaId,
            @RequestParam("pageNumber") long pageNumber,
            @RequestParam("image") MultipartFile file
    ) {
        return service.createPage(mangaId, chapterId, pageNumber, credentials, file);
    }

    @DeleteMapping("/{mangaId}/chapters/{chapterId}/{pageId}")
    public void deletePage(
            @AuthenticationPrincipal UserCredentials credentials,
            @PathVariable("mangaId") UUID chapterId,
            @PathVariable("chapterId") UUID mangaId,
            @PathVariable("pageId") UUID pageId
    ) {
        service.deletePage(credentials, mangaId, chapterId, pageId);
    }

    @GetMapping("/{mangaId}/chapters/{chapterId}")
    public Page<ChapterPageDTO> getChapterPages(
            @PathVariable("mangaId") UUID mangaId,
            @PathVariable("chapterId") UUID chapterId,
            @AuthenticationPrincipal UserCredentials credentials,
            @RequestParam(value = "size", defaultValue = "20")
            @Min(1) @Max(1000) int size,
            @RequestParam(value = "num", defaultValue = "0")
            @PositiveOrZero int num,
            @RequestParam(value = "orderBy", defaultValue = "DESC")
            @Pattern(
                    regexp = "asc|desc",
                    flags = {Pattern.Flag.CASE_INSENSITIVE},
                    message = "Direction can be only ASC or DESC"
            )
            String order,
            @RequestParam(value = "properties", defaultValue = "pageNumber")
            String[] properties
    ) {
        return service.getChapterPages(
                mangaId,
                chapterId,
                credentials,
                PageRequest.of(num, size, Sort.Direction.fromString(order), properties)
        );
    }

    @GetMapping("/{mangaId}/chapters/{chapterId}/{pageId}")
    public FullChapterPageDTO getChapterPage(
            @PathVariable("mangaId") UUID mangaId,
            @PathVariable("chapterId") UUID chapterId,
            @PathVariable("pageId") UUID pageId,
            @AuthenticationPrincipal UserCredentials credentials
    ) {
        return service.getChapterPage(
                mangaId,
                chapterId,
                credentials,
                pageId
        );
    }
}
