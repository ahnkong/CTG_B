package com.hoolhool.backend.entity;

import java.time.LocalDateTime;

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
    @Column(name = "comment_id", nullable = false)
    private Long commentId;

    @Column(name = "board_id", nullable = false)
    private Long boardId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "co_c_date", nullable = false)
    private LocalDateTime coCDate;

    @Column(name = "co_likes", columnDefinition = "INTEGER")
    private Integer coLikes;

    @Column(name = "recomment_id", nullable = false)
    private Long reCommentId;
}
