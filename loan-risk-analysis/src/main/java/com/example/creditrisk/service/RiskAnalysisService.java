package com.example.creditrisk.service;

import com.example.creditrisk.model.CreditRiskData;
import com.example.creditrisk.enums.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class RiskAnalysisService {

    @Value("${app.batch.risk.thresholds.low}")
    private int lowThreshold;

    @Value("${app.batch.risk.thresholds.medium}")
    private int mediumThreshold;

    @Value("${app.batch.risk.thresholds.high}")
    private int highThreshold;

    @Value("${app.batch.risk.weights.credit-score}")
    private double creditScoreWeight;

    @Value("${app.batch.risk.weights.income}")
    private double incomeWeight;

    @Value("${app.batch.risk.weights.debt-ratio}")
    private double debtRatioWeight;

    @Value("${app.batch.risk.weights.payment-history}")
    private double paymentHistoryWeight;

    @Cacheable(value = "riskScores", key = "#creditRiskData.customerId")
    public Map<String, Object> analyzeRisk(CreditRiskData creditRiskData) {
        Map<String, Object> analysis = new HashMap<>();

        double baseScore = calculateBaseScore(creditRiskData);

        double incomeRiskFactor = calculateIncomeRiskFactor(creditRiskData);
        double debtRiskFactor = calculateDebtRiskFactor(creditRiskData);
        double paymentRiskFactor = calculatePaymentRiskFactor(creditRiskData);

        double finalScore = calculateFinalScore(baseScore, incomeRiskFactor, debtRiskFactor, paymentRiskFactor);

        RiskCategory riskCategory = determineRiskCategory(finalScore);

        double defaultProbability = calculateDefaultProbability(finalScore);

        analysis.put(AnalysisKey.BASE_SCORE.getValue(), baseScore);
        analysis.put(AnalysisKey.INCOME_RISK_FACTOR.getValue(), incomeRiskFactor);
        analysis.put(AnalysisKey.DEBT_RISK_FACTOR.getValue(), debtRiskFactor);
        analysis.put(AnalysisKey.PAYMENT_RISK_FACTOR.getValue(), paymentRiskFactor);
        analysis.put(AnalysisKey.FINAL_SCORE.getValue(), finalScore);
        analysis.put(AnalysisKey.RISK_CATEGORY.getValue(), riskCategory.getValue());
        analysis.put(AnalysisKey.DEFAULT_PROBABILITY.getValue(), defaultProbability);
        analysis.put(AnalysisKey.RECOMMENDATIONS.getValue(), generateRecommendations(analysis));

        return analysis;
    }

    private double calculateBaseScore(CreditRiskData data) {
        double normalizedCreditScore = (data.getCreditScore() / 850.0) * 100;

        return normalizedCreditScore * creditScoreWeight;
    }

    private double calculateIncomeRiskFactor(CreditRiskData data) {
        double normalizedIncome = Math.min(data.getIncome() / 200000.0, 1.0);

        double incomeRisk = 1.0 - normalizedIncome;

        return incomeRisk * incomeWeight;
    }

    private double calculateDebtRiskFactor(CreditRiskData data) {
        double debtRatio = data.getDebtToIncomeRatio();

        double normalizedDebtRatio = Math.min(debtRatio / 100.0, 1.0);

        return normalizedDebtRatio * debtRatioWeight;
    }

    private double calculatePaymentRiskFactor(CreditRiskData data) {
        double normalizedPaymentHistory = data.getPaymentHistory() / 100.0;

        double paymentRisk = 1.0 - normalizedPaymentHistory;

        return paymentRisk * paymentHistoryWeight;
    }

    private double calculateFinalScore(double baseScore, double incomeRisk, double debtRisk, double paymentRisk) {
        double riskScore = baseScore - (incomeRisk + debtRisk + paymentRisk) * 100;

        return Math.max(0, Math.min(100, riskScore));
    }

    private RiskCategory determineRiskCategory(double finalScore) {
        if (finalScore >= lowThreshold) {
            return RiskCategory.LOW;
        } else if (finalScore >= mediumThreshold) {
            return RiskCategory.MEDIUM;
        } else if (finalScore >= highThreshold) {
            return RiskCategory.HIGH;
        } else {
            return RiskCategory.VERY_HIGH;
        }
    }

    private double calculateDefaultProbability(double finalScore) {

        return 1.0 / (1.0 + Math.exp((finalScore - 50) / 10));
    }

    private Map<String, String> generateRecommendations(Map<String, Object> analysis) {
        Map<String, String> recommendations = new HashMap<>();
        RiskCategory riskCategory = RiskCategory.valueOf((String) analysis.get(AnalysisKey.RISK_CATEGORY.getValue()));
        double defaultProbability = (Double) analysis.get(AnalysisKey.DEFAULT_PROBABILITY.getValue());

        switch (riskCategory) {
            case LOW:
                recommendations.put(RecommendationType.CREDIT_LIMIT.getValue(), "Increase credit limit");
                recommendations.put(RecommendationType.INTEREST_RATE.getValue(), "Offer preferential rate");
                recommendations.put(RecommendationType.TERMS.getValue(), "Flexible terms");
                break;
            case MEDIUM:
                recommendations.put(RecommendationType.CREDIT_LIMIT.getValue(), "Maintain current limit");
                recommendations.put(RecommendationType.INTEREST_RATE.getValue(), "Standard rate");
                recommendations.put(RecommendationType.TERMS.getValue(), "Standard terms");
                break;
            case HIGH:
                recommendations.put(RecommendationType.CREDIT_LIMIT.getValue(), "Reduce credit limit");
                recommendations.put(RecommendationType.INTEREST_RATE.getValue(), "Higher rate");
                recommendations.put(RecommendationType.TERMS.getValue(), "Stricter terms");
                break;
            case VERY_HIGH:
                recommendations.put(RecommendationType.CREDIT_LIMIT.getValue(), "Reject application");
                recommendations.put(RecommendationType.INTEREST_RATE.getValue(), "Not applicable");
                recommendations.put(RecommendationType.TERMS.getValue(), "Not applicable");
                break;
        }

        if ((Double) analysis.get(AnalysisKey.INCOME_RISK_FACTOR.getValue()) > 0.7) {
            recommendations.put(RecommendationType.INCOME.getValue(), "Request additional income proof");
        }
        if ((Double) analysis.get(AnalysisKey.DEBT_RISK_FACTOR.getValue()) > 0.7) {
            recommendations.put(RecommendationType.DEBT.getValue(), "Recommend debt reduction");
        }
        if ((Double) analysis.get(AnalysisKey.PAYMENT_RISK_FACTOR.getValue()) > 0.7) {
            recommendations.put(RecommendationType.PAYMENT.getValue(), "Suggest structured payment plan");
        }

        return recommendations;
    }
}