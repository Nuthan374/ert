package com.example.creditrisk.enums;

public enum CreditRiskColumn {
    CUSTOMER_ID("customerId"),
    CUSTOMER_NAME("customerName"),
    CREDIT_SCORE("creditScore"),
    INCOME("income"),
    DEBT_TO_INCOME_RATIO("debtToIncomeRatio"),
    PAYMENT_HISTORY("paymentHistory"),
    EMPLOYMENT_YEARS("employmentYears"),
    LOAN_AMOUNT("loanAmount"),
    LOAN_PURPOSE("loanPurpose"),
    EXISTING_LOANS("existingLoans"),
    PROPERTY_VALUE("propertyValue"),
    MARITAL_STATUS("maritalStatus"),
    EDUCATION_LEVEL("educationLevel"),
    INDUSTRY("industry"),
    RISK_CATEGORY("riskCategory"),
    STATUS("status"),
    ADDITIONAL_INFO("additionalInfo"),
    RECOMMENDATIONS("recommendations"),
    BIRTH_DATE("birthDate"),
    AGE("age"),
    EMPLOYMENT_TYPE("employmentType"),
    MONTHLY_EXPENSES("monthlyExpenses"),
    SAVINGS_BALANCE("savingsBalance"),
    CREDIT_HISTORY_YEARS("creditHistoryYears"),
    NUMBER_OF_CREDIT_CARDS("numberOfCreditCards"),
    CREDIT_CARD_UTILIZATION("creditCardUtilization"),
    HAS_BANKRUPTCY("hasBankruptcy"),
    BANKRUPTCY_YEARS_AGO("bankruptcyYearsAgo"),
    HAS_FORECLOSURE("hasForeclosure"),
    FORECLOSURE_YEARS_AGO("foreclosureYearsAgo"),
    RESIDENCE_TYPE("residenceType"),
    YEARS_AT_CURRENT_ADDRESS("yearsAtCurrentAddress"),
    LOAN_TERM("loanTerm"),
    INTEREST_RATE("interestRate"),
    COLLATERAL_TYPE("collateralType"),
    COLLATERAL_VALUE("collateralValue"),
    GUARANTOR_STATUS("guarantorStatus"),
    GUARANTOR_CREDIT_SCORE("guarantorCreditScore"),
    GUARANTOR_INCOME("guarantorIncome"),
    GUARANTOR_RELATIONSHIP("guarantorRelationship");

    private final String columnName;

    CreditRiskColumn(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }

    public static String[] getAllColumnNames() {
        CreditRiskColumn[] columns = values();
        String[] names = new String[columns.length];
        for (int i = 0; i < columns.length; i++) {
            names[i] = columns[i].getColumnName();
        }
        return names;
    }
} 