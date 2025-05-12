package com.ctg.backend.entity;

public enum UserRole {
    // 최고 관리자
    SUPER_ADMIN("최고 관리자"),

    // 교회 관리자 (목사진, 전도사)
    CHURCH_ADMIN("교회 관리자"),

    // 교회 매니저 (장로, 권사)
    CHURCH_MANAGER("교회 매니저"),

    // 목사진
    SENIOR_PASTOR("담임목사"),
    ASSISTANT_PASTOR("부목사"),
    PROBATION_PASTOR("강도사"),
    JUNIOR_PASTOR("전도사"),

    // 장로, 권사
    ELDER_MALE("장로"),
    ELDER_FEMALE("권사"),

    // 집사
    SERVANT("집사"),

    // 일반 성도
    MEMBER("성도");

    private final String description;

    UserRole(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    // 권한 체크 메서드들
    public boolean canManageAllChurches() {
        return this == SUPER_ADMIN;
    }

    public boolean canManageChurch() {
        return this == SUPER_ADMIN || this == CHURCH_ADMIN;
    }

    public boolean canManageNotice() {
        return this == SUPER_ADMIN || this == CHURCH_ADMIN || this == CHURCH_MANAGER;
    }

    public boolean canManageWorship() {
        return this == SUPER_ADMIN || this == CHURCH_ADMIN;
    }

    public boolean canManageNewsletter() {
        return this == SUPER_ADMIN || this == CHURCH_ADMIN;
    }

    public boolean canManageCommunity() {
        return true; // 모든 사용자가 커뮤니티 게시판 사용 가능
    }

    public boolean canApproveMembers() {
        return this == SUPER_ADMIN || this == CHURCH_ADMIN;
    }

    public boolean canBlockMembers() {
        return this == SUPER_ADMIN || this == CHURCH_ADMIN;
    }

    public boolean canBlockPosts() {
        return this == SUPER_ADMIN || this == CHURCH_ADMIN;
    }
} 