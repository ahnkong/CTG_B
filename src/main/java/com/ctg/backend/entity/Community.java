package com.ctg.backend.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "community")
@Getter
@Setter
public class Community {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long boardId;

    @Column
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "community_type")
    private CommunityType communityType;

    @Enumerated(EnumType.STRING)
    @Column(name = "content_status")
    private ContentStatus contentStatus;

    @Column
    private Integer view = 0;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "domain_id", nullable = false)
    private Domain domain;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL)
    private List<Image> images;

    @OneToMany(mappedBy = "community", cascade = CascadeType.ALL)
    private List<Like> likes;
} 