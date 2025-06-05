package com.ctg.backend.entity;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "notice")
@Getter
@Setter
public class Notice {
    
    public enum NoticeStatus {
        UPCOMING,   // 시작 전
        ONGOING,    // 진행 중
        ENDED       // 마감됨
    }
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long boardId;

    @Column
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(name = "notice_type")
    private NoticeType noticeType;

    @Column(name = "display_start_date")
    private LocalDateTime displayStartDate;

    @Column(name = "display_end_date")
    private LocalDateTime displayEndDate;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "content_status")
    private ContentStatus contentStatus = ContentStatus.ACTIVE;

    @Column
    private Integer view = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "domain_id", nullable = false)
    private Domain domain;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL)
    private List<Comment> comments;

    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL)
    private List<Image> images;

    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL)
    private List<Like> likes;

    @Enumerated(EnumType.STRING)
    @Column(name = "notice_status")
    private NoticeStatus noticeStatus = NoticeStatus.UPCOMING;

    public void updateStatus() {
        LocalDateTime now = LocalDateTime.now();
        
        if (displayStartDate == null || displayEndDate == null) {
            this.noticeStatus = NoticeStatus.ONGOING;
            return;
        }
        
        if (now.isBefore(displayStartDate)) {
            this.noticeStatus = NoticeStatus.UPCOMING;
        } else if (now.isAfter(displayEndDate)) {
            this.noticeStatus = NoticeStatus.ENDED;
        } else {
            this.noticeStatus = NoticeStatus.ONGOING;
        }
    }
} 