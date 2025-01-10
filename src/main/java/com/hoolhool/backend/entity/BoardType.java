package com.hoolhool.backend.entity;

import java.util.Arrays;

public enum BoardType {
    POSITIVE,
    NEGATIVE;

    public static boolean isValid(String type) {
        return Arrays.stream(BoardType.values())
                     .anyMatch(e -> e.name().equalsIgnoreCase(type));
    }
}
