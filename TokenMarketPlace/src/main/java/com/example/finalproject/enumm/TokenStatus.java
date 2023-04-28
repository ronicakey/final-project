package com.example.finalproject.enumm;

import java.util.HashMap;
import java.util.Map;

public enum TokenStatus {
    PENDING("НА МОДЕРАЦИИ"),
    FOR_SALE("ПРОДАЁТСЯ"),
    NOT_FOR_SALE("НЕ ПРОДАЁТСЯ"),
    BLOCKED("ЗАБЛОКИРОВАН"),
    PENDING_ORDER("ЗАКАЗ НА МОДЕРАЦИИ");

    private static final Map<String, TokenStatus> BY_LABEL = new HashMap<>();

    static {
        for (TokenStatus s : values()) {
            BY_LABEL.put(s.label, s);
        }
    }

    private final String label;

    TokenStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }

    public static TokenStatus valueOfLabel(String label) {
        return BY_LABEL.get(label);
    }
}
