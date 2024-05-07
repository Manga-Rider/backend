package com.mangarider.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "t_manga_comments")
public class MangaComment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID commentId;

    @ManyToOne
    @JoinColumn(name = "manga_id")
    private Manga manga;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private User authorId;

    @Column(nullable = false)
    private String content;

    private Status status;

    @CreatedDate
    private LocalDateTime createdAt;

    public enum Status {
        DEFAULT,
        EDITED,
        REMOVED // removed by admin
    }
}
