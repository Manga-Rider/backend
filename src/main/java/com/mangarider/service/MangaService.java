package com.mangarider.service;

import com.mangarider.exception.ForbiddenAccessException;
import com.mangarider.exception.InvalidMangaException;
import com.mangarider.exception.UserNotFoundException;
import com.mangarider.mapper.MangaMapper;
import com.mangarider.model.dto.FullMangaDTO;
import com.mangarider.model.dto.MangaDTO;
import com.mangarider.model.dto.request.MangaRequest;
import com.mangarider.model.entity.Manga;
import com.mangarider.model.entity.User;
import com.mangarider.model.entity.UserCredentials;
import com.mangarider.repository.MangaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static com.mangarider.model.entity.Manga.Status.DRAFT;
import static com.mangarider.model.entity.Manga.Status.REMOVED;

@Service
@RequiredArgsConstructor
public class MangaService {
    private final MangaRepository repository;
    private final MangaMapper mapper;
    private final ImageService imageService;

    @Transactional(readOnly = true)
    public Page<MangaDTO> getPage(Pageable page) {
        return repository.findAllWhereStatusNot(List.of(DRAFT, REMOVED), page)
                .map(manga -> mapper.toDTO(manga, imageService.getUrl(manga.getCover())));
    }

    @Transactional
    public MangaDTO create(UserCredentials credentials, MangaRequest request) {
        User user = credentials.getUser();
        Manga manga = Manga.builder()
                .author(user)
                .description(request.description())
                .title(request.title())
                .build();
        manga = repository.save(manga);

        return mapper.toDTO(manga, null);
    }

    @Transactional
    public void uploadCoverImage(UUID mangaId, UserCredentials credentials, MultipartFile file) {
        Manga manga = getMangaAndCheckOwner(mangaId, credentials);
        imageService.setCoverImage(credentials.getUserId(), manga, file);
    }

    @Transactional
    public void uploadImages(UUID mangaId, UserCredentials credentials, MultipartFile[] files) {
        Manga manga = getMangaAndCheckOwner(mangaId, credentials);
        imageService.addImages(credentials.getUserId(), manga, files);
    }

    @Transactional(readOnly = true)
    public FullMangaDTO getManga(UUID mangaId, UserCredentials credentials) {
        Manga manga = getManga(mangaId);
        if (isAuthor(manga, credentials)) {
            return mapper.toFullDTO(manga, imageService.getUrl(manga.getCover()), imageService.getUrls(manga.getImages()));
        }
        if (!manga.getStatus().isPublic()) {
            throw new ForbiddenAccessException(HttpStatus.FORBIDDEN, "Access denied");
        }
        return mapper.toFullDTO(manga, imageService.getUrl(manga.getCover()), imageService.getUrls(manga.getImages()));
    }

    @Transactional
    public void edit(UserCredentials credentials, UUID mangaId, MangaRequest request, Manga.Status status) {
        Manga manga = getMangaAndCheckOwner(mangaId, credentials);
        if (request.description() != null) {
            manga.setDescription(request.description());
        }
        if (request.title() != null) {
            manga.setTitle(request.title());
        }
        if (status != null) {
            switch (status) {
                case PUBLISHED, ON_GOING, FINISHED -> {
                    if (manga.getTitle() == null || manga.getCover() == null || manga.getDescription() == null) {
                        throw new InvalidMangaException("Not full data provided");
                    }
                    manga.setStatus(status);
                    if (manga.getPublishedAt() == null) {
                        manga.setPublishedAt(LocalDateTime.now());
                    }
                }
                case DRAFT -> {
                    manga.setStatus(status);
                }
                case REMOVED -> {
                    throw new ForbiddenAccessException(HttpStatus.FORBIDDEN, "Access denied");
                }
            }
        }
    }

    private boolean isAuthor(Manga manga, UserCredentials credentials) {
        return manga.getAuthor().getUserId().equals(credentials.getUserId());
    }

    private Manga getMangaAndCheckOwner(UUID mangaId, UserCredentials credentials) {
        Manga manga = getManga(mangaId);
        if (!manga.getAuthor().getUserId().equals(credentials.getUserId())) {
            throw new ForbiddenAccessException(HttpStatus.FORBIDDEN, "Access denied");
        }
        return manga;
    }

    private Manga getManga(UUID mangaId) {
        return repository.findById(mangaId)
                .orElseThrow(() -> new UserNotFoundException("Manga with id = {%s} not found".formatted(mangaId)));
    }
}
