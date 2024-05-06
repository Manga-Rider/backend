package com.mangarider.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder

@Entity
@Table(name = "t_manga_chapters")
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public UUID chapterId;

    @ManyToOne(optional = false)
    public Manga manga;

    public String title;

    @Builder.Default
    public long views = 0L;

    @Builder.Default
    public Status status = Status.DRAFT;

    public LocalDate releaseDate;

    @CreatedDate
    public LocalDateTime createdAt;

    public enum Status {
        PUBLISHED,
        DRAFT,
        REMOVED // by admin
    }
}
