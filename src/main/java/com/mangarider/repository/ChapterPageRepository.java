package com.mangarider.repository;

import com.mangarider.model.entity.Chapter;
import com.mangarider.model.entity.ChapterPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface ChapterPageRepository extends JpaRepository<ChapterPage, UUID> {
    @Query(value = """
            SELECT COUNT(*) FROM t_chapter_pages AS page WHERE page.chapter_id = ?1
            """, nativeQuery = true)
    long countByChapterId(UUID chapterId);

    @Query("select c from ChapterPage c where c.chapter.chapterId = ?1")
    Page<ChapterPage> findPagesByChapter(UUID chapterId, Pageable pageable);

    @Query("select c from ChapterPage c where c.chapter.chapterId = ?1 and c.chapter.status not in ?2")
    Page<ChapterPage> findByChapterWhereStatusNotIn(UUID chapterId, Collection<Chapter.Status> statuses, Pageable pageable);

    @Query("select (count(c) > 0) from ChapterPage c where c.chapter.chapterId = ?1 and c.pageNumber = ?2")
    boolean existsByChapterAndPageNumber(UUID chapterId, long pageNumber);

    @Transactional
    @Modifying
    @Query("delete from ChapterPage c where c.chapter = ?1")
    int deleteAllByChapter(Chapter chapter);

    @Query("select c from ChapterPage c where c.chapter = ?1")
    List<ChapterPage> findByChapter(Chapter chapter);
}
