package com.mangarider.service;

import com.mangarider.mapper.MangaMapper;
import com.mangarider.model.dto.MangaDTO;
import com.mangarider.model.dto.request.CreateMangaRequest;
import com.mangarider.model.entity.Manga;
import com.mangarider.model.entity.User;
import com.mangarider.model.entity.UserCredentials;
import com.mangarider.repository.MangaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MangaService {
    private final MangaRepository repository;
    private final MangaMapper mapper;
    private final ImageService imageService;

    @Transactional(readOnly = true)
    public Page<MangaDTO> getPage(Pageable page) {
        return repository.findAll(page)
                .map(manga -> mapper.toDTO(manga, imageService.getUrl(manga.getCover())));
    }

//    public Manga create(UserCredentials credentials, CreateMangaRequest request) {
//        User user = credentials.getUser();
//        Manga manga = Manga.builder()
//                .author(user)
//                .description(request)
//                .build();
//    }

//    public Manga createManga(CreateMangaRequest data) {
//
//    }
}
