package com.example.creditrisk.controller;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import java.util.List;
import java.util.Map;

import com.example.creditrisk.model.CreditRiskData;
import com.example.creditrisk.repository.CreditRiskDataRepository;

@RestController
@RequestMapping("/api/credit-risk")
public class CreditRiskController {

    private final JobLauncher jobLauncher;
    private final Job creditRiskAnalysisJob;
    private final CreditRiskDataRepository repository;

    public CreditRiskController(JobLauncher jobLauncher,
            Job creditRiskAnalysisJob,
            CreditRiskDataRepository repository) {
        this.jobLauncher = jobLauncher;
        this.creditRiskAnalysisJob = creditRiskAnalysisJob;
        this.repository = repository;
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadCsv(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is empty");
            }

            String projectDir = System.getProperty("user.dir");
            Path inputPath = Paths.get(projectDir, "src", "main", "resources", "input");

            if (!Files.exists(inputPath)) {
                Files.createDirectories(inputPath);
            }

            Path targetFile = inputPath.resolve("credit-risk-data.csv");
            Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);

            JobParameters jobParameters = new JobParametersBuilder()
                    .addLong("time", System.currentTimeMillis())
                    .toJobParameters();

            JobExecution jobExecution = jobLauncher.run(creditRiskAnalysisJob, jobParameters);

            while (jobExecution.isRunning()) {
                Thread.sleep(1000);
            }

            if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
                return ResponseEntity.ok(Map.of(
                        "status", "success",
                        "message", "File processed successfully",
                        "recordsProcessed", jobExecution.getStepExecutions().iterator().next().getWriteCount()));
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(Map.of("status", "error", "message", "Job failed"));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("status", "error", "message", e.getMessage()));
        }
    }

    @GetMapping("/results")
    public ResponseEntity<List<CreditRiskData>> getResults() {
        List<CreditRiskData> results = repository.findAll();
        return ResponseEntity.ok(results);
    }

    @GetMapping("/results/download")
    public ResponseEntity<Resource> downloadResults() {
        try {
            String projectDir = System.getProperty("user.dir");
            Path csvPath = Paths.get(projectDir, "output", "credit-risk-results.csv");

            if (!Files.exists(csvPath)) {
                return ResponseEntity.notFound().build();
            }

            Resource resource = new FileSystemResource(csvPath.toFile());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"credit-risk-results.csv\"")
                    .contentType(MediaType.parseMediaType("text/csv"))
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}