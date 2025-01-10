package com.hoolhool.backend.entity;

import java.util.Arrays;

public enum LikeType {
    BOARD,
    COMMENT,
    RECOMMENT;

    // 유효성 검사 메서드
    public static boolean isValid(String value) {
        return Arrays.stream(LikeType.values())
                     .anyMatch(type -> type.name().equalsIgnoreCase(value));
    }
}
