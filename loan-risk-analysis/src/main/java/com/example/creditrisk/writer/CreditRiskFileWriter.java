package com.example.creditrisk.writer;

import com.example.creditrisk.model.CreditRiskData;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
public class CreditRiskFileWriter extends FlatFileItemWriter<CreditRiskData> {

    public CreditRiskFileWriter() {
        setupWriter();
    }

    private void setupWriter() {
        try {
            String projectDir = System.getProperty("user.dir");

            Path outputPath = Paths.get(projectDir, "output");

            if (!Files.exists(outputPath)) {
                Files.createDirectories(outputPath);
                System.out.println("Directory created: " + outputPath);
            }

            File outputFile = outputPath.resolve("credit-risk-results.csv").toFile();

            if (outputFile.exists()) {
                boolean deleted = outputFile.delete();
                if (deleted) {
                    System.out.println("Existing file deleted: " + outputFile.getAbsolutePath());
                } else {
                    System.out.println("Failed to delete existing file: " + outputFile.getAbsolutePath());
                }
            }

            System.out.println("Output file configured at: " + outputFile.getAbsolutePath());

            setResource(new FileSystemResource(outputFile));
            setAppendAllowed(false);

            DelimitedLineAggregator<CreditRiskData> lineAggregator = new DelimitedLineAggregator<>();
            lineAggregator.setDelimiter(",");

            BeanWrapperFieldExtractor<CreditRiskData> fieldExtractor = new BeanWrapperFieldExtractor<>();
            fieldExtractor.setNames(new String[] { "customerId", "customerName", "creditScore", "income",
                    "debtToIncomeRatio", "paymentHistory", "riskCategory" });
            lineAggregator.setFieldExtractor(fieldExtractor);

            setLineAggregator(lineAggregator);

            setHeaderCallback(writer -> writer
                    .write("customerId,customerName,creditScore,income,debtToIncomeRatio,paymentHistory,riskCategory"));

            setFooterCallback(writer -> writer.write("End of file"));

            afterPropertiesSet();

            System.out.println("Writer successfully configured for file: " + outputFile.getAbsolutePath());

        } catch (Exception e) {
            throw new RuntimeException("Error configuring the writer: " + e.getMessage(), e);
        }
    }
}