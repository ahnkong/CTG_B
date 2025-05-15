package com.ctg.backend.entity;

public enum UserRole {
    // 최고 관리자
    SUPER_ADMIN("최고 관리자"),

    // 교회 관리자 (목사진, 전도사)
    // CHURCH_ADMIN("교회 관리자"),

    // 교회 매니저 (장로, 권사)
    // CHURCH_MANAGER("교회 매니저"),

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


    public boolean canCrudVideoBoard() {
        // SUPER_ADMIN, 목사진만 CRUD 가능
        return this == SUPER_ADMIN ||
               this == SENIOR_PASTOR ||
               this == ASSISTANT_PASTOR ||
               this == PROBATION_PASTOR ||
               this == JUNIOR_PASTOR;
    }

    public boolean canReadOrLikeVideoBoard() {
        // 모든 사용자 가능
        return true;
    }

    public boolean canCrudNoticeBoard() {
        // SUPER_ADMIN, 목사진, 장로, 권사만 CRUD 가능
        return this == SUPER_ADMIN ||
               this == SENIOR_PASTOR ||
               this == ASSISTANT_PASTOR ||
               this == PROBATION_PASTOR ||
               this == JUNIOR_PASTOR ||
               this == ELDER_MALE ||
               this == ELDER_FEMALE ;
    }

    public boolean canReadOrLikeNoticeBoard() {
        // 모든 사용자 가능
        return true;
    }

    public boolean canCrudNewsletterBoard() {
        // SUPER_ADMIN, 목사진만 CRUD 가능
        return this == SUPER_ADMIN ||
               this == SENIOR_PASTOR ||
               this == ASSISTANT_PASTOR ||
               this == PROBATION_PASTOR ||
               this == JUNIOR_PASTOR;
    }

    public boolean canReadOrLikeNewsletterBoard() {
        // 모든 사용자 가능
        return true;
    }

} 