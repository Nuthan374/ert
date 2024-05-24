package com.example.creditrisk.enums;

public enum RecommendationType {
    CREDIT_LIMIT("creditLimit"),
    INTEREST_RATE("interestRate"),
    TERMS("terms"),
    INCOME("income"),
    DEBT("debt"),
    PAYMENT("payment");

    private final String value;

    RecommendationType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
} 