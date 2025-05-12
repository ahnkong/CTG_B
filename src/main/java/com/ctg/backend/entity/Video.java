package com.ctg.backend.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "video")
@Getter
@Setter
public class Video {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long boardId;

    @Column
    private String title;

    @Column(name = "video_url")
    private String videoUrl;

    @Column
    private String peacher;

    @Column(name = "video_date")
    private LocalDate videoDate;

    @Column
    private String reference;

    @Column(name = "sub_title", columnDefinition = "TEXT")
    private String subTitle;

    @Column(name = "view", nullable = false)
    private Integer view = 0;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "content_status", nullable = false)
    private ContentStatus contentStatus = ContentStatus.ACTIVE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "domain_id", nullable = false)
    private Domain domain;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL)
    private List<Like> likes;
} 