package com.mangarider.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_manga_chapters")
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "chapter_id")
    private UUID chapterId;

    @ManyToOne(optional = false)
    @JoinColumn(name = "manga_id", nullable = false)
    private Manga manga;

    private String title;

    @Builder.Default
    private long views = 0L;

    @Builder.Default
    private Status status = Status.DRAFT;

    private LocalDate releaseDate;

    @CreatedDate
    private LocalDateTime createdAt;

    public enum Status {
        PUBLISHED,
        DRAFT,
        REMOVED // by admin
    }


}
