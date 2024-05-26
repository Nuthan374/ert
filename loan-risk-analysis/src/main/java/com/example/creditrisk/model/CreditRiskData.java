package com.example.creditrisk.model;

import com.example.creditrisk.enums.*;
import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "credit_risk_data")
@Data
public class CreditRiskData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "customer_id")
    private String customerId;
    
    @Column(name = "customer_name")
    private String customerName;
    
    @Column(name = "credit_score")
    private Integer creditScore;
    
    @Column(name = "income")
    private Double income;
    
    @Column(name = "debt_to_income_ratio")
    private Double debtToIncomeRatio;
    
    @Column(name = "payment_history")
    private Integer paymentHistory;
    
    @Column(name = "employment_years")
    private Integer employmentYears;
    
    @Column(name = "loan_amount")
    private Double loanAmount;
    
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "loan_purpose")
    private LoanPurpose loanPurpose;
    
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "employment_type")
    private EmploymentType employmentType;
    
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "education_level")
    private EducationLevel educationLevel;
    
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "marital_status")
    private MaritalStatus maritalStatus;
    
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "residence_type")
    private ResidenceType residenceType;
    
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "loan_term")
    private LoanTerm loanTerm;
    
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "collateral_type")
    private CollateralType collateralType;
    
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "guarantor_status")
    private GuarantorStatus guarantorStatus;
    
    @Column(name = "existing_loans")
    private Integer existingLoans;
    
    @Column(name = "property_value")
    private Double propertyValue;
    
    @Column(name = "industry")
    private String industry;
    
    @Column(name = "risk_category")
    private String riskCategory;
    
    @Column(name = "status")
    private String status;
    
    @Column(name = "additional_info")
    private String additionalInfo;
    
    @Column(name = "recommendations", length = 1000)
    private String recommendations;
    
    @Column(name = "birth_date")
    private LocalDate birthDate;
    
    @Column(name = "age")
    private Integer age;
    
    @Column(name = "monthly_expenses")
    private Double monthlyExpenses;
    
    @Column(name = "savings_balance")
    private Double savingsBalance;
    
    @Column(name = "credit_history_years")
    private Integer creditHistoryYears;
    
    @Column(name = "number_of_credit_cards")
    private Integer numberOfCreditCards;
    
    @Column(name = "credit_card_utilization")
    private Double creditCardUtilization;
    
    @Column(name = "has_bankruptcy")
    private Boolean hasBankruptcy;
    
    @Column(name = "bankruptcy_years_ago")
    private Integer bankruptcyYearsAgo;
    
    @Column(name = "has_foreclosure")
    private Boolean hasForeclosure;
    
    @Column(name = "foreclosure_years_ago")
    private Integer foreclosureYearsAgo;
    
    @Column(name = "years_at_current_address")
    private Integer yearsAtCurrentAddress;
    
    @Column(name = "interest_rate")
    private Double interestRate;
    
    @Column(name = "collateral_value")
    private Double collateralValue;
    
    @Column(name = "guarantor_credit_score")
    private Integer guarantorCreditScore;
    
    @Column(name = "guarantor_income")
    private Double guarantorIncome;
    
    @Column(name = "guarantor_relationship")
    private String guarantorRelationship;
} 