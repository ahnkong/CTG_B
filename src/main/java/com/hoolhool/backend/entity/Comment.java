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
@Table(name = "comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id", nullable = false)
    private Long commentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", referencedColumnName = "board_id", nullable = false)
    private Board board;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "co_c_date", nullable = false)
    private LocalDateTime coCDate;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReComment> reComments;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes; // 좋아요 관계 유지

    // 좋아요 개수 반환
    public Long getLikeCount() {
        return this.likes != null ? (long) this.likes.size() : 0L;
    }

    public Comment(Long commentId, Board board, String userId, String content, LocalDateTime coCDate) {
        this.commentId = commentId;
        this.board = board;
        this.userId = userId;
        this.content = content;
        this.coCDate = coCDate;
    }

    
}
