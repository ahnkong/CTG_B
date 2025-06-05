package com.ctg.backend.entity;

import java.time.LocalDateTime;
import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "book_unified")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookUnified {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long bookId;

    @Column(nullable = false)
    private String title;

    private String author;

    private String isbn;

    private String publisher;

    @Column(name = "chapter_number")
    private Integer chapterNumber;

    @Column(name = "chapter_title")
    private String chapterTitle;

    @Column(name = "section_number")
    private Integer sectionNumber;

    @Lob
    private String content;

    @Lob
    private String reference;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
