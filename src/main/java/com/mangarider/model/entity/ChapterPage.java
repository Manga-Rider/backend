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
@Table(name = "t_chapter_pages")
public class ChapterPage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID pageId;

    @ManyToOne
    @JoinColumn(name = "chapter_id", referencedColumnName = "chapter_id")
    private Chapter chapter;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "content_id", referencedColumnName = "image_id")
    private Image content;

    private long pageNumber;

    @CreatedDate
    private LocalDateTime createdAt;
}
