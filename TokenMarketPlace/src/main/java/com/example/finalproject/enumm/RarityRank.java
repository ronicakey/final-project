package com.example.finalproject.enumm;

import java.util.HashMap;
import java.util.Map;

public enum RarityRank {
    COMMON("Обычный"),
    RARE("Редкий"),
    EPIC("Очень редкий");

    private static final Map<String, RarityRank> BY_LABEL = new HashMap<>();

    static {
        for (RarityRank s : values()) {
            BY_LABEL.put(s.label, s);
        }
    }

    private final String label;

    RarityRank(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }

    public static RarityRank valueOfLabel(String label) {
        return BY_LABEL.get(label);
    }
}
