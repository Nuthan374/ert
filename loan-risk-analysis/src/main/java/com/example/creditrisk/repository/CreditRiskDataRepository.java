package com.example.creditrisk.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import com.example.creditrisk.model.CreditRiskData; 

@Repository
public interface CreditRiskDataRepository extends JpaRepository<CreditRiskData, Long> {

    @Query("SELECT c FROM CreditRiskData c ORDER BY c.id DESC")
    List<CreditRiskData> findAllOrderByIdDesc();

    @Query("SELECT c FROM CreditRiskData c WHERE c.riskCategory = :riskCategory")
    List<CreditRiskData> findByRiskCategory(@Param("riskCategory") String riskCategory);

    @Query("SELECT c.riskCategory, COUNT(*) FROM CreditRiskData c GROUP BY c.riskCategory")
    List<Object[]> getRiskCategorySummary();
}