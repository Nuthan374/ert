package com.example.creditrisk.enums;

public enum RiskCategory {
    LOW("LOW"),
    MEDIUM("MEDIUM"),
    HIGH("HIGH"),
    VERY_HIGH("VERY_HIGH");

    private final String value;

    RiskCategory(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
} 