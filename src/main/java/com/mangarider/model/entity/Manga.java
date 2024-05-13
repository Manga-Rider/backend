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

    @OneToOne
    @JoinColumn(name = "manga_cover_image_id", referencedColumnName = "image_id")
    private Image cover;

    @CreatedDate
    private LocalDateTime createdAt;

    @Enumerated(EnumType.ORDINAL)
    @Builder.Default
    private Status status = Status.DRAFT;

    @Builder.Default
    private long views = 0L;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "manga_id")
    private List<Image> images = new ArrayList<>();

    public void addImage(Image image) {
        images.add(image);
    }

    public void removeImage(Image image) {
        images.remove(image);
    }

    public enum Status {
        PUBLISHED,
        DRAFT,
        ON_GOING,
        FINISHED,
        REMOVED; // by admin

        public boolean isPublic() {
            return !this.equals(DRAFT) && !this.equals(REMOVED);
        }
    }
}
