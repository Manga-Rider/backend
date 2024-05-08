package com.mangarider.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_mangas")
public class Manga {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID mangaId;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

    private String title;

    private String description;

    private LocalDateTime publishedAt;

    @CreatedDate
    private LocalDateTime createdAt;

    @Enumerated(EnumType.ORDINAL)
    @Builder.Default
    public Status status = Status.DRAFT;

    @Builder.Default
    private long views = 0L;

    @OneToMany(mappedBy = "manga", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MangaImage> images = new ArrayList<>();

    public void addImage(MangaImage image) {
        images.add(image);
        image.setManga(this);
    }

    public void removeComment(MangaImage image) {
        images.remove(image);
        image.setManga(null);
    }

    public enum Status {
        PUBLISHED,
        DRAFT,
        ON_GOING,
        REMOVED // by admin
    }
}
