package com.mangarider.repository;

import com.mangarider.model.entity.Manga;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MangaRepository extends JpaRepository<Manga, UUID> {
}
