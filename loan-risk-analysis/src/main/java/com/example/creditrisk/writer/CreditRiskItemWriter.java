package com.example.creditrisk.writer;

import com.example.creditrisk.model.CreditRiskData;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityManagerFactory;

@Component
public class CreditRiskItemWriter extends JpaItemWriter<CreditRiskData> {
    
    public CreditRiskItemWriter(EntityManagerFactory entityManagerFactory) {
        setEntityManagerFactory(entityManagerFactory);
    }
} 