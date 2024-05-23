package com.example.creditrisk.config;

import com.example.creditrisk.model.CreditRiskData;
import com.example.creditrisk.processor.CreditRiskProcessor;
import com.example.creditrisk.reader.CreditRiskItemReader;
import com.example.creditrisk.writer.CreditRiskFileWriter;
import com.example.creditrisk.writer.CreditRiskItemWriter;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

@Configuration
public class BatchConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final CreditRiskItemReader reader;
    private final CreditRiskProcessor processor;
    private final CreditRiskItemWriter dbWriter;
    private final CreditRiskFileWriter fileWriter;

    public BatchConfig(JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            CreditRiskItemReader reader,
            CreditRiskProcessor processor,
            CreditRiskItemWriter dbWriter,
            CreditRiskFileWriter fileWriter) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.reader = reader;
        this.processor = processor;
        this.dbWriter = dbWriter;
        this.fileWriter = fileWriter;
    }

    @Bean
    public JobExecutionListener jobExecutionListener() {
        return new JobExecutionListenerSupport() {
            @Override
            public void afterJob(org.springframework.batch.core.JobExecution jobExecution) {
                System.out.println("Job completed with status: " + jobExecution.getStatus());

                String projectDir = System.getProperty("user.dir");
                Path outputPath = Paths.get(projectDir, "output");

                File outputFile = outputPath.toFile();

                if (outputFile.exists()) {
                    System.out.println("Output file generated at: " + outputFile.getAbsolutePath());
                    System.out.println("File size: " + outputFile.length() + " bytes");
                } else {
                    System.err.println(
                            "WARNING! The output file was not generated at: " + outputFile.getAbsolutePath());
                }
            }
        };
    }

    @Bean
    public Job creditRiskAnalysisJob() {
        return new JobBuilder("creditRiskAnalysisJob", jobRepository)
                .start(creditRiskAnalysisStep())
                .listener(jobExecutionListener())
                .build();
    }

    @Bean
    public Step creditRiskAnalysisStep() {
        CompositeItemWriter<CreditRiskData> compositeWriter = new CompositeItemWriter<>();
        compositeWriter.setDelegates(Arrays.asList(dbWriter, fileWriter));

        TaskletStep step = new StepBuilder("creditRiskAnalysisStep", jobRepository)
                .<CreditRiskData, CreditRiskData>chunk(10, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(compositeWriter)
                .build();

        // Add listener for logging
        step.registerStepExecutionListener(new org.springframework.batch.core.StepExecutionListener() {
            @Override
            public void beforeStep(org.springframework.batch.core.StepExecution stepExecution) {
                System.out.println("Starting step: " + stepExecution.getStepName());
            }

            @Override
            public org.springframework.batch.core.ExitStatus afterStep(
                    org.springframework.batch.core.StepExecution stepExecution) {
                System.out.println("Step completed: " + stepExecution.getStepName());
                System.out.println("Records read: " + stepExecution.getReadCount());
                System.out.println("Records written: " + stepExecution.getWriteCount());
                return stepExecution.getExitStatus();
            }
        });

        return step;
    }

    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor executor = new SimpleAsyncTaskExecutor();
        executor.setConcurrencyLimit(1);
        return executor;
    }

}