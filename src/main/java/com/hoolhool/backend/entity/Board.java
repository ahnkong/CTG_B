package com.hoolhool.backend.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "board")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id", nullable = false)
    private Long boardId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "c_date", nullable = false)
    private LocalDateTime cDate;

    @Column(name = "hashtag")
    private String hashTag;

    @Column(name = "hidden", columnDefinition = "TINYINT DEFAULT 0")
    private Boolean hidden;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "view", columnDefinition = "INTEGER DEFAULT 0")
    private Integer view;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private BoardType type;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;

    @Column(name = "status", columnDefinition = "VARCHAR(20) DEFAULT 'PUBLISHED'")
    private String status; // 상태: DRAFT or PUBLISHED

    @Column(name = "last_saved_at", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private LocalDateTime lastSavedAt; // 마지막 임시 저장 시간

    // 좋아요 개수 반환 (정렬을 위해 활용 가능)
    public Long getLikeCount() {
        return this.likes != null ? (long) this.likes.size() : 0L;
    }

    public Board(Long boardId, String userId, String content, LocalDateTime cDate, 
                 String hashTag, Boolean hidden, String title, Integer view, 
                 BoardType type, String status, LocalDateTime lastSavedAt) {
        this.boardId = boardId;
        this.userId = userId;
        this.content = content;
        this.cDate = cDate;
        this.hashTag = hashTag;
        this.hidden = hidden;
        this.title = title;
        this.view = view;
        this.type = type;
        this.status = status;
        this.lastSavedAt = lastSavedAt;
    }
}
