package com.example.creditrisk.reader;

import com.example.creditrisk.model.CreditRiskData;
import com.example.creditrisk.enums.*;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class CreditRiskFieldSetMapper implements FieldSetMapper<CreditRiskData> {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    @Override
    public CreditRiskData mapFieldSet(FieldSet fieldSet) throws BindException {
        CreditRiskData data = new CreditRiskData();
        
        data.setCustomerId(fieldSet.readString(CreditRiskColumn.CUSTOMER_ID.getColumnName()));
        data.setCustomerName(fieldSet.readString(CreditRiskColumn.CUSTOMER_NAME.getColumnName()));
        data.setCreditScore(fieldSet.readInt(CreditRiskColumn.CREDIT_SCORE.getColumnName()));
        data.setIncome(fieldSet.readDouble(CreditRiskColumn.INCOME.getColumnName()));
        data.setDebtToIncomeRatio(fieldSet.readDouble(CreditRiskColumn.DEBT_TO_INCOME_RATIO.getColumnName()));
        data.setPaymentHistory(fieldSet.readInt(CreditRiskColumn.PAYMENT_HISTORY.getColumnName()));
        data.setEmploymentYears(fieldSet.readInt(CreditRiskColumn.EMPLOYMENT_YEARS.getColumnName()));
        data.setLoanAmount(fieldSet.readDouble(CreditRiskColumn.LOAN_AMOUNT.getColumnName()));
        
        String loanPurpose = fieldSet.readString(CreditRiskColumn.LOAN_PURPOSE.getColumnName());
        if (loanPurpose != null && !loanPurpose.isEmpty()) {
            try {
                if ("Mortgage".equals(loanPurpose)) {
                    data.setLoanPurpose(LoanPurpose.MORTGAGE);
                } else {
                    String normalizedPurpose = loanPurpose.toUpperCase()
                        .replace("-", "_")
                        .replace(" ", "_");
                    data.setLoanPurpose(LoanPurpose.valueOf(normalizedPurpose));
                }
            } catch (IllegalArgumentException e) {
                data.setLoanPurpose(LoanPurpose.PERSONAL);
            }
        }
        
        try {
            String employmentType = fieldSet.readString(CreditRiskColumn.EMPLOYMENT_TYPE.getColumnName());
            if (employmentType != null && !employmentType.isEmpty()) {
                data.setEmploymentType(EmploymentType.valueOf(
                    employmentType.toUpperCase().replace("-", "_").replace(" ", "_")));
            }
        } catch (IllegalArgumentException e) {
            data.setEmploymentType(EmploymentType.FULL_TIME);
        }
        
        try {
            String educationLevel = fieldSet.readString(CreditRiskColumn.EDUCATION_LEVEL.getColumnName());
            if (educationLevel != null && !educationLevel.isEmpty()) {
                data.setEducationLevel(EducationLevel.valueOf(
                    educationLevel.toUpperCase().replace("-", "_").replace(" ", "_")));
            }
        } catch (IllegalArgumentException e) {
            data.setEducationLevel(EducationLevel.BACHELORS);
        }
        
        try {
            String maritalStatus = fieldSet.readString(CreditRiskColumn.MARITAL_STATUS.getColumnName());
            if (maritalStatus != null && !maritalStatus.isEmpty()) {
                data.setMaritalStatus(MaritalStatus.valueOf(
                    maritalStatus.toUpperCase().replace("-", "_").replace(" ", "_")));
            }
        } catch (IllegalArgumentException e) {
            data.setMaritalStatus(MaritalStatus.SINGLE);
        }
        
        try {
            String residenceType = fieldSet.readString(CreditRiskColumn.RESIDENCE_TYPE.getColumnName());
            if (residenceType != null && !residenceType.isEmpty()) {
                data.setResidenceType(ResidenceType.valueOf(
                    residenceType.toUpperCase().replace("-", "_").replace(" ", "_")));
            }
        } catch (IllegalArgumentException e) {
            data.setResidenceType(ResidenceType.OWN);
        }
        
        try {
            String loanTerm = fieldSet.readString(CreditRiskColumn.LOAN_TERM.getColumnName());
            if (loanTerm != null && !loanTerm.isEmpty()) {
                data.setLoanTerm(LoanTerm.valueOf(
                    loanTerm.toUpperCase().replace("-", "_").replace(" ", "_")));
            }
        } catch (IllegalArgumentException e) {
            data.setLoanTerm(LoanTerm.MEDIUM_TERM);
        }
        
        try {
            String collateralType = fieldSet.readString(CreditRiskColumn.COLLATERAL_TYPE.getColumnName());
            if (collateralType != null && !collateralType.isEmpty()) {
                data.setCollateralType(CollateralType.valueOf(
                    collateralType.toUpperCase().replace("-", "_").replace(" ", "_")));
            }
        } catch (IllegalArgumentException e) {
            data.setCollateralType(CollateralType.NONE);
        }
        
        try {
            String guarantorStatus = fieldSet.readString(CreditRiskColumn.GUARANTOR_STATUS.getColumnName());
            if (guarantorStatus != null && !guarantorStatus.isEmpty()) {
                data.setGuarantorStatus(GuarantorStatus.valueOf(
                    guarantorStatus.toUpperCase().replace("-", "_").replace(" ", "_")));
            }
        } catch (IllegalArgumentException e) {
            data.setGuarantorStatus(GuarantorStatus.NONE);
        }
        
        data.setExistingLoans(fieldSet.readInt(CreditRiskColumn.EXISTING_LOANS.getColumnName()));
        data.setPropertyValue(fieldSet.readDouble(CreditRiskColumn.PROPERTY_VALUE.getColumnName()));
        data.setIndustry(fieldSet.readString(CreditRiskColumn.INDUSTRY.getColumnName()));
        data.setRiskCategory(fieldSet.readString(CreditRiskColumn.RISK_CATEGORY.getColumnName()));
        data.setStatus(fieldSet.readString(CreditRiskColumn.STATUS.getColumnName()));
        data.setAdditionalInfo(fieldSet.readString(CreditRiskColumn.ADDITIONAL_INFO.getColumnName()));
        data.setRecommendations(fieldSet.readString(CreditRiskColumn.RECOMMENDATIONS.getColumnName()));
        
        String birthDateStr = fieldSet.readString(CreditRiskColumn.BIRTH_DATE.getColumnName());
        if (birthDateStr != null && !birthDateStr.isEmpty()) {
            data.setBirthDate(LocalDate.parse(birthDateStr, DATE_FORMATTER));
        }
        data.setAge(fieldSet.readInt(CreditRiskColumn.AGE.getColumnName()));
        
        data.setMonthlyExpenses(fieldSet.readDouble(CreditRiskColumn.MONTHLY_EXPENSES.getColumnName()));
        data.setSavingsBalance(fieldSet.readDouble(CreditRiskColumn.SAVINGS_BALANCE.getColumnName()));
        data.setCreditHistoryYears(fieldSet.readInt(CreditRiskColumn.CREDIT_HISTORY_YEARS.getColumnName()));
        data.setNumberOfCreditCards(fieldSet.readInt(CreditRiskColumn.NUMBER_OF_CREDIT_CARDS.getColumnName()));
        data.setCreditCardUtilization(fieldSet.readDouble(CreditRiskColumn.CREDIT_CARD_UTILIZATION.getColumnName()));
        
        data.setHasBankruptcy(fieldSet.readBoolean(CreditRiskColumn.HAS_BANKRUPTCY.getColumnName()));
        data.setBankruptcyYearsAgo(fieldSet.readInt(CreditRiskColumn.BANKRUPTCY_YEARS_AGO.getColumnName()));
        data.setHasForeclosure(fieldSet.readBoolean(CreditRiskColumn.HAS_FORECLOSURE.getColumnName()));
        data.setForeclosureYearsAgo(fieldSet.readInt(CreditRiskColumn.FORECLOSURE_YEARS_AGO.getColumnName()));
        
        data.setYearsAtCurrentAddress(fieldSet.readInt(CreditRiskColumn.YEARS_AT_CURRENT_ADDRESS.getColumnName()));
        
        data.setInterestRate(fieldSet.readDouble(CreditRiskColumn.INTEREST_RATE.getColumnName()));
        data.setCollateralValue(fieldSet.readDouble(CreditRiskColumn.COLLATERAL_VALUE.getColumnName()));
        
        String guarantorCreditScore = fieldSet.readString(CreditRiskColumn.GUARANTOR_CREDIT_SCORE.getColumnName());
        if (guarantorCreditScore != null && !guarantorCreditScore.isEmpty()) {
            data.setGuarantorCreditScore(Integer.parseInt(guarantorCreditScore));
        }
        
        String guarantorIncome = fieldSet.readString(CreditRiskColumn.GUARANTOR_INCOME.getColumnName());
        if (guarantorIncome != null && !guarantorIncome.isEmpty()) {
            data.setGuarantorIncome(Double.parseDouble(guarantorIncome));
        }
        
        data.setGuarantorRelationship(fieldSet.readString(CreditRiskColumn.GUARANTOR_RELATIONSHIP.getColumnName()));
        
        return data;
    }
} 