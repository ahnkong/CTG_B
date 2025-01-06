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
@Table(name = "recomment")
public class ReComment {
    @Id
    @Column(name = "recomment_id", nullable = false)
    private Long recommentId;

    @Column(name = "comment_id", nullable = false)
    private Long commentId;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "content", nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "re_c_date", nullable = false)
    private LocalDateTime reCDate;

    @Column(name = "re_likes", columnDefinition = "INTEGER")
    private Integer reLikes;
}
