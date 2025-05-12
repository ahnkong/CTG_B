package com.ctg.backend.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long commentId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "content_status", nullable = false)
    private ContentStatus contentStatus = ContentStatus.ACTIVE;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "board_type")
    private BoardType boardType;

    @Column(name = "board_id")
    private Long boardId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "community_id")
    private Community community;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "notice_id")
    private Notice notice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "newsletter_id")
    private Newsletter newsletter;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id")
    private Video video;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    private List<ReComment> reComments;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    private List<Like> likes;

    // 좋아요 개수 반환
    public Long getLikeCount() {
        return this.likes != null ? (long) this.likes.size() : 0L;
    }

    public Comment(Long commentId, String content, LocalDateTime createdAt, ContentStatus contentStatus, LocalDateTime updatedAt) {
        this.commentId = commentId;
        this.content = content;
        this.createdAt = createdAt;
        this.contentStatus = contentStatus;
        this.updatedAt = updatedAt;
    }
}
