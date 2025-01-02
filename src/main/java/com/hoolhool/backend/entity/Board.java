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
@Table(name = "board")
public class Board {
    @Id
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

    @Column(name = "type")
    private String type;

    @Column(name = "comment_id", nullable = true)
    private Long commentId;
}
