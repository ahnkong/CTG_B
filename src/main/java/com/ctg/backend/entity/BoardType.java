package com.ctg.backend.entity;

import java.util.Arrays;

public enum BoardType {
    COMMUNITY,  // 커뮤니티
    NOTICE,     // 공지사항
    NEWSLETTER, // 주보
    WORSHIP;    // 예배 영상

    public static boolean isValid(String type) {
        return Arrays.stream(BoardType.values())
                     .anyMatch(e -> e.name().equalsIgnoreCase(type));
    }
}
