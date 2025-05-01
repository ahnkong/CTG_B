package com.ctg.backend.entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

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

    @Column(name = "churchName", nullable = true)
    private String churchName;


    @Column(name = "grade", nullable = true)
    private String grade;

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

    @Enumerated(EnumType.STRING)
    @Column(name = "role", columnDefinition = "ENUM('USER', 'ADMIN') DEFAULT 'USER'")
    private Role role;

    @Column(name = "is_active", columnDefinition = "TINYINT DEFAULT 1")
    private Boolean isActive;

    @Enumerated(EnumType.STRING)
    @Column(name = "mbti", columnDefinition = "ENUM('INTP', 'INTJ', 'INFP', 'INFJ', 'ISTP', 'ISTJ', 'ISFP', 'ISFJ', 'ENTP', 'ENTJ', 'ENFP', 'ENFJ', 'ESTP', 'ESTJ', 'ESFP', 'ESFJ')")
    private MBTI mbti;

    @Column(name = "personal")
    private String personal;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PointTransaction> pointTransactions;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserPurchase> purchases;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserQuestTransaction> questTransactions;

    public String getSocialType() {
        throw new UnsupportedOperationException("Unimplemented method 'getSocialType'");
    }


    public Long getPoint() {
        return point;
    }
    
    public void setPoint(Long point) {
        this.point = point;
    }
    

}
