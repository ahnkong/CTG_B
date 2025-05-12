package com.ctg.backend.entity;

import javax.persistence.*;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import com.ctg.backend.entity.Role;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false, unique = true)
    private String email;

    @Column
    private String password;

    @Column(nullable = false)
    private String name;

    @Column
    private Date birth;

    @Column
    private String nickname;

    @Column
    private String tell;

    @Column
    private String info;

    @Column(nullable = false)
    private Integer local; // 1=로컬, 2=구글, 3=네이버, 4=카카오

    @Column
    private String providerId; //OAuth provider에서 내려주는 고유 id

    @Column
    private String profileImage;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private Boolean isActive = true;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role = Role.USER;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "domain_id")
    private Domain domain;

    @Column(nullable = false)
    private Boolean agreeToTerms = false;

    @Column(nullable = false)
    private Boolean agreeToMarketing = false;

    @LastModifiedDate
    private LocalDateTime updatedAt;

}
