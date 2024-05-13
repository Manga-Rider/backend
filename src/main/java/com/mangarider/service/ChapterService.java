package com.mangarider.service;

import com.mangarider.exception.ConflictException;
import com.mangarider.exception.ForbiddenAccessException;
import com.mangarider.exception.InvalidMangaException;
import com.mangarider.exception.NotFoundException;
import com.mangarider.mapper.MangaMapper;
import com.mangarider.model.dto.ChapterDTO;
import com.mangarider.model.dto.ChapterPageDTO;
import com.mangarider.model.dto.FullChapterPageDTO;
import com.mangarider.model.dto.request.ChapterCreationRequest;
import com.mangarider.model.dto.request.ChapterEditRequest;
import com.mangarider.model.entity.*;
import com.mangarider.repository.ChapterPageRepository;
import com.mangarider.repository.ChapterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChapterService {
    private final MangaService mangaService;
    private final ImageService imageService;
    private final ChapterPageRepository pageRepository;
    private final ChapterRepository chapterRepository;
    private final MangaMapper mapper;

    @Transactional(readOnly = true)
    public Page<ChapterDTO> getChapters(UUID mangaId, UserCredentials credentials, Pageable page) {
        Manga manga = mangaService.getPublicManga(mangaId, credentials);

        if (mangaService.isAuthor(manga, credentials)) {
            Page<Chapter> chapters = chapterRepository.findByMangaId(manga.getMangaId(), page);
            return chapters.map(chapter -> mapper.toDTO(chapter, pageRepository.countByChapterId(chapter.getChapterId())));
        }
        Page<Chapter> chapters = chapterRepository
                .findByMangaIdWhereStatusNot(mangaId, List.of(Chapter.Status.DRAFT, Chapter.Status.REMOVED), page);

        return chapters.map(chapter -> mapper.toDTO(chapter, pageRepository.countByChapterId(chapter.getChapterId())));
    }

    @Transactional(readOnly = true)
    public Page<ChapterPageDTO> getChapterPages(UUID mangaId, UUID chapterId, UserCredentials credentials, Pageable page) {
        Manga manga = mangaService.getManga(mangaId);

        if (mangaService.isAuthor(manga, credentials)) {
            Page<ChapterPage> chapterPages = pageRepository.findPagesByChapter(chapterId, page);
            return chapterPages.map(mapper::toDTO);
        }
        Page<ChapterPage> chapterPages = pageRepository
                .findByChapterWhereStatusNotIn(chapterId, List.of(Chapter.Status.DRAFT, Chapter.Status.REMOVED), page);

        return chapterPages.map(mapper::toDTO);
    }

    @Transactional
    public ChapterDTO createChapter(UUID mangaId, UserCredentials credentials, ChapterCreationRequest request) {
        Manga manga = getMangaCheckOwner(credentials, mangaId);
        Chapter chapter = createChapter(manga, request);
        return mapper.toDTO(chapter, 0);
    }

    @Transactional
    public ChapterDTO editChapter(UserCredentials credentials, UUID mangaId, UUID chapterId, ChapterEditRequest request) {
        Manga manga = getMangaCheckOwner(credentials, mangaId);
        Chapter chapter = getChapter(chapterId);

        if (request.title() != null) {
            manga.setTitle(request.title());
        }
        if (request.status() != null) {
            switch (request.status()) {
                case PUBLISHED -> {
                    if (chapter.getTitle() == null) {
                        throw new InvalidMangaException("Not full data provided");
                    }

                    chapter.setStatus(request.status());
                    if (chapter.getReleaseDate() == null) {
                        chapter.setReleaseDate(LocalDate.now());
                    }
                }
                case DRAFT -> chapter.setStatus(request.status());
                case REMOVED -> throw new ForbiddenAccessException("Access denied");
            }
        }

        return mapper.toDTO(chapter,  pageRepository.countByChapterId(chapter.getChapterId()));
    }

    @Transactional
    public ChapterPageDTO createPage(UUID mangaId,
                                     UUID chapterId,
                                     long pageNumber,
                                     UserCredentials credentials,
                                     MultipartFile file) {
        Manga manga = getMangaCheckOwner(credentials, mangaId);

        Chapter chapter = getChapter(chapterId);
        if (pageRepository.existsByChapterAndPageNumber(chapterId, pageNumber)) {
            throw new ConflictException("Page with number = {%s} already exists on this chapter".formatted(pageNumber));
        }

        Image content = imageService.create(credentials.getUserId(), mangaId, file);
        ChapterPage page = createPage(chapter, content, pageNumber);

        return mapper.toDTO(page);
    }

    @Transactional
    public void deletePage(UserCredentials credentials, UUID mangaId, UUID chapterId, UUID pageId) {
        Manga manga = getMangaCheckOwner(credentials, mangaId);
        Chapter chapter = getChapter(chapterId);
        ChapterPage page = getChapterPage(pageId);

        imageService.delete(page.getContent());
        pageRepository.delete(page);
    }

    @Transactional
    public void deleteChapter(UserCredentials credentials, UUID mangaId, UUID chapterId) {
        Manga manga = getMangaCheckOwner(credentials, mangaId);
        Chapter chapter = getChapter(chapterId);

        List<ChapterPage> pages = pageRepository.findByChapter(chapter);
        for (var page : pages) {
            imageService.delete(page.getContent());
        }
        pageRepository.deleteAllByChapter(chapter);
    }

    private Manga getMangaCheckOwner(UserCredentials credentials, UUID mangaId) {
        Manga manga = mangaService.getManga(mangaId);

        if (!mangaService.isAuthor(manga, credentials)) {
            throw new ForbiddenAccessException("Invalid credentials");
        }
        return manga;
    }

    @Transactional(readOnly = true)
    public FullChapterPageDTO getChapterPage(UUID mangaId, UUID chapterId, UserCredentials credentials, UUID pageId) {
        Manga manga = mangaService.getPublicManga(mangaId, credentials);
        ChapterPage page = pageRepository.findById(pageId)
                .orElseThrow(() -> new NotFoundException("Page with id = {%s} not found".formatted(pageId)));

        if (mangaService.isAuthor(manga, credentials)) {
            return mapper.toFullDTO(page, imageService.getUrl(page.getContent()));
        }

        if (!page.getChapter().getStatus().equals(Chapter.Status.PUBLISHED)) {
            throw new ForbiddenAccessException(HttpStatus.FORBIDDEN, "Access denied");
        }

        return mapper.toFullDTO(page, imageService.getUrl(page.getContent()));
    }

    private Chapter createChapter(Manga manga, ChapterCreationRequest request) {
        return chapterRepository.save(
                Chapter.builder()
                        .manga(manga)
                        .title(request.title())
                        .build()
        );
    }

    private ChapterPage createPage(Chapter chapter, Image content, long pageNumber) {
        return pageRepository.save(
                ChapterPage.builder()
                        .chapter(chapter)
                        .content(content)
                        .pageNumber(pageNumber)
                        .build()
        );
    }

    private Chapter getChapter(UUID chapterId) {
        return chapterRepository.findById(chapterId)
                .orElseThrow(() -> new NotFoundException("Chapter with id = {%s} not found".formatted(chapterId)));
    }

    private ChapterPage getChapterPage(UUID pageId) {
        return pageRepository.findById(pageId)
                .orElseThrow(() -> new NotFoundException("Page with id = {%s} not found".formatted(pageId)));
    }
}
