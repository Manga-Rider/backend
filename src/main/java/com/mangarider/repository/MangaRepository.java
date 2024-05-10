package com.mangarider.repository;

import com.mangarider.model.entity.Manga;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.UUID;

public interface MangaRepository extends JpaRepository<Manga, UUID> {

    @Query(value = "SELECT * FROM t_mangas AS m WHERE m.status != ANY(?1)", nativeQuery = true)
    Page<Manga> findAllWhereStatusNot(Collection<Manga.Status> statuses, Pageable pageable);
}
