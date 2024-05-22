package com.example.creditrisk.enums;

public enum AnalysisKey {
    BASE_SCORE("baseScore"),
    INCOME_RISK_FACTOR("incomeRiskFactor"),
    DEBT_RISK_FACTOR("debtRiskFactor"),
    PAYMENT_RISK_FACTOR("paymentRiskFactor"),
    FINAL_SCORE("finalScore"),
    RISK_CATEGORY("riskCategory"),
    DEFAULT_PROBABILITY("defaultProbability"),
    RECOMMENDATIONS("recommendations");

    private final String value;

    AnalysisKey(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
} 