package com.mangarider.controller;

import com.mangarider.model.dto.FullMangaDTO;
import com.mangarider.model.dto.MangaDTO;
import com.mangarider.model.dto.request.MangaRequest;
import com.mangarider.model.entity.Manga;
import com.mangarider.model.entity.UserCredentials;
import com.mangarider.service.MangaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/mangas")
@RequiredArgsConstructor
@Validated
public class MangaController {
    private final MangaService service;

    @GetMapping
    public Page<MangaDTO> getMangas(
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
            @RequestParam(value = "properties", defaultValue = "createdAt")
            String[] properties
    ) {
        return service.getPage(PageRequest.of(num, size, Sort.Direction.fromString(order), properties));
    }

    @GetMapping("/{mangaId}")
    public FullMangaDTO getManga(
            @PathVariable("mangaId") UUID mangaId,
            @AuthenticationPrincipal UserCredentials credentials
    ) {
        return service.getManga(mangaId, credentials);
    }

    @GetMapping("/authors/{authorId}")
    public Page<MangaDTO> getPersonMangas(
            @PathVariable("authorId") UUID authorId,
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
            @RequestParam(value = "properties", defaultValue = "createdAt")
            String[] properties,
            @AuthenticationPrincipal UserCredentials credentials
    ) {
        return service.getAuthorMangas(authorId, credentials, PageRequest.of(num, size, Sort.Direction.fromString(order), properties));
    }

    @PostMapping
    public MangaDTO create(
            @Valid @RequestBody MangaRequest request,
            @AuthenticationPrincipal UserCredentials credentials
    ) {
        return service.create(credentials, request);
    }

    @PutMapping("/{mangaId}")
    public void edit(
            @AuthenticationPrincipal UserCredentials credentials,
            @PathVariable("mangaId") UUID mangaId,
            @RequestParam(name = "status", required = false) Manga.Status status,
            @Valid @RequestBody MangaRequest request
    ) {
        service.edit(credentials, mangaId, request, status);
    }

    @PutMapping("/{mangaId}/coverImage")
    public void uploadCoverImage(
            @AuthenticationPrincipal UserCredentials credentials,
            @RequestParam(name = "file") MultipartFile file,
            @PathVariable("mangaId") UUID mangaId
    ) {
        service.uploadCoverImage(mangaId, credentials, file);
    }

    @PutMapping("/{mangaId}/image")
    public void addImage(
            @AuthenticationPrincipal UserCredentials credentials,
            @RequestParam(name = "files") MultipartFile[] files,
            @PathVariable("mangaId") UUID mangaId
    ) {
        service.uploadImages(mangaId, credentials, files);
    }
}
