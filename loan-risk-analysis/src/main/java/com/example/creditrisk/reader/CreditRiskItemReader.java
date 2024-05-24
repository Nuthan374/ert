package com.example.creditrisk.reader;

import com.example.creditrisk.model.CreditRiskData;
import com.example.creditrisk.enums.CreditRiskColumn;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.stereotype.Component;
import org.springframework.core.io.FileSystemResource;

@Component
public class CreditRiskItemReader extends FlatFileItemReader<CreditRiskData> {
    
    public CreditRiskItemReader() {
        setResource(new FileSystemResource("src/main/resources/input/credit-risk-data.csv"));
        setLinesToSkip(1);
        
        DefaultLineMapper<CreditRiskData> lineMapper = new DefaultLineMapper<>();
        
        DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
        tokenizer.setNames(CreditRiskColumn.getAllColumnNames());
        
        tokenizer.setStrict(false);
        
        BeanWrapperFieldSetMapper<CreditRiskData> fieldSetMapper = new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(CreditRiskData.class);
        
        lineMapper.setLineTokenizer(tokenizer);
        lineMapper.setFieldSetMapper(new CreditRiskFieldSetMapper());
        
        setLineMapper(lineMapper);
    }
} 