package com.mangarider.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder

@Entity
@Table(name = "t_chapter_comments")
public class ChapterComment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID commentId;

    @ManyToOne
    private Chapter chapter;

    @ManyToOne
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
