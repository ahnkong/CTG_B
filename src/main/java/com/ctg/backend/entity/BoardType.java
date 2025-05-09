package com.ctg.backend.entity;

import java.util.Arrays;

public enum BoardType {
    GENERAL,
    NOTICE;

    public static boolean isValid(String type) {
        return Arrays.stream(BoardType.values())
                     .anyMatch(e -> e.name().equalsIgnoreCase(type));
    }
}
