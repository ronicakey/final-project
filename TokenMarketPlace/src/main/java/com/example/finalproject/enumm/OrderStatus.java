package com.example.finalproject.enumm;

import java.util.HashMap;
import java.util.Map;

public enum OrderStatus {
    PENDING("На модерации"),
    COMPLETED("Завершён"),
    BLOCKED("Заблокирован");

    private static final Map<String, OrderStatus> BY_LABEL = new HashMap<>();

    static {
        for (OrderStatus s : values()) {
            BY_LABEL.put(s.label, s);
        }
    }

    private final String label;

    OrderStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }

    public static OrderStatus valueOfLabel(String label) {
        return BY_LABEL.get(label);
    }
}
