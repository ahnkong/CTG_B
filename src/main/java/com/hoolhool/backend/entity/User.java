package com.hoolhool.backend.entity;

import javax.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User {
    @Id
    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "birth")
    private Date birth;

    @Column(name = "gender")
    private String gender;

    @Column(name = "info")
    private String info;

    @Column(name = "marketing")
    private Boolean marketing;

    @Column(name = "local", columnDefinition = "INTEGER DEFAULT 1")
    private Integer local;

    @Column(name = "tell", nullable = false)
    private String tell;

    @Column(name = "profileimage")
    private String profileImage;

    @Column(name = "point", columnDefinition = "BIGINT DEFAULT 0")
    private Long point;

    @Column(name = "u_date", nullable = false)
    private LocalDateTime uDate;

    @Column(name = "role", columnDefinition = "ENUM('USER', 'ADMIN') DEFAULT 'USER'")
    private String role;

    @Column(name = "is_active", columnDefinition = "TINYINT DEFAULT 1")
    private Boolean isActive;

    @Column(name = "mbti", columnDefinition = "ENUM('INTP', 'INTJ', 'INFP', 'INFJ', 'ISTP', 'ISTJ', 'ISFP', 'ISFJ', 'ENTP', 'ENTJ', 'ENFP', 'ENFJ', 'ESTP', 'ESTJ', 'ESFP', 'ESFJ')")
    private String mbti;

    @Column(name = "personal")
    private String personal;

    @Column(name = "board_id", nullable = true)
    private Long boardId;

    @Column(name = "transaction_id", nullable = true)
    private Long transactionId;

    @Column(name = "user_quest_id", nullable = true)
    private Long userQuestId;

    @Column(name = "purchase_id", nullable = true)
    private Long purchaseId;
}
