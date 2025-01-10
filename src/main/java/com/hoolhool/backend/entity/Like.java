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
@Table(name = "likes")
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id", nullable = false)
    private Long likeId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false, columnDefinition = "ENUM('BOARD', 'COMMENT', 'RECOMMENT')")
    private LikeType type;

    @Column(name = "target_id", nullable = false)
    private String targetId;

    @Column(name = "like_date", nullable = false)
    private LocalDateTime likeDate;

    @Column(name = "board_id")
    private Long boardId;

    @Column(name = "comment_id")
    private Long commentId;

    @Column(name = "recomment_id")
    private Long recommentId;
}