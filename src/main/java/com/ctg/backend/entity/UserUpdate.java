package com.ctg.backend.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "UserUpdate")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdate {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role = UserRole.MEMBER;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserStatus status = UserStatus.PENDING;

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(name = "joined_at")
    private LocalDateTime joinedAt = LocalDateTime.now();

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "domain_id", nullable = false)
    private Domain domain;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // User의 Role이 ADMIN인 경우 자동으로 SUPER_ADMIN 권한 부여
    @PrePersist
    @PreUpdate
    public void setRoleBasedOnUserRole() {
        if (user != null && user.getRole().equals(Role.ADMIN)) {
            this.role = UserRole.SUPER_ADMIN;
            this.status = UserStatus.APPROVED;
        }
    }

    // 권한 체크 메서드들
    public boolean canManageAllChurches() {
        return role.canManageAllChurches();
    }

    public boolean canManageChurch() {
        return role.canManageChurch();
    }

    public boolean canManageNotice() {
        return role.canManageNotice();
    }

    public boolean canManageWorship() {
        return role.canManageWorship();
    }

    public boolean canManageNewsletter() {
        return role.canManageNewsletter();
    }

    public boolean canManageCommunity() {
        return role.canManageCommunity();
    }

    public boolean canApproveMembers() {
        return role.canApproveMembers();
    }

    public boolean canBlockMembers() {
        return role.canBlockMembers();
    }

    public boolean canBlockPosts() {
        return role.canBlockPosts();
    }
}
