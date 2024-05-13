package com.mangarider.repository;

import com.mangarider.model.entity.Chapter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.UUID;

public interface ChapterRepository extends JpaRepository<Chapter, UUID> {
    @Query("select c from Chapter c where c.manga.mangaId = ?1")
    Page<Chapter> findByMangaId(UUID mangaId, Pageable pageable);

    @Query("select c from Chapter c where c.manga.mangaId = ?1 and c.status not in ?2")
    Page<Chapter> findByMangaIdWhereStatusNot(UUID mangaId, Collection<Chapter.Status> statuses, Pageable pageable);
}
