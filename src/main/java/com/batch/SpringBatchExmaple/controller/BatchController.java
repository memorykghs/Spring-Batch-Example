package com.batch.SpringBatchExample.controller;

import java.time.LocalDate;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobInstanceAlreadyExistsException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BatchController2 {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(BatchController2.class);
    
    @Autowired
    private JobOperator jobOperator;
    
    @Autowired
    private JobRegistry jobRegistry;

    @Autowired
    private JobLauncher jobLauncher;
    
    @GetMapping(value = "/testGroupItem")
    public void testGroupItem() throws Exception {
        try {
            String params = new StringBuilder()
                    .append("today=20211109").append(',')
                    .append("tomorrow=20211110")
                    .toString();
            jobOperator.start("testGroupItem", params);
        } catch (JobInstanceAlreadyExistsException e) {
            LOGGER.debug("Job實例已存在，產生新的實例繼續執行", e);
            jobOperator.startNextInstance("testGroupItem");
        }
    }
    
    /**
     * <pre>
     * 和testColumnMapRowMapperJob2啟動相同的Job
     * 用來測試Process的StepScope
     * </pre>
     * @throws Exception
     */
    @GetMapping(value = "/testColumnMapRowMapperJob")
    public void testColumnMapRowMapperJob() throws Exception {
        Job job = jobRegistry.getJob("testColumnMapRowMapper");
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("exeDate", LocalDate.now().toString())
                .addDate("exeTime", new Date(), true)
                .toJobParameters();
        jobLauncher.run(job, jobParameters);
    }
    
    /**
     * <pre>
     * 和testColumnMapRowMapperJob啟動相同的Job
     * 用來測試Process的StepScope
     * </pre>
     * @throws Exception
     */
    @GetMapping(value = "/testColumnMapRowMapperJob2")
    public void testColumnMapRowMapperJob2() throws Exception {
        Job job = jobRegistry.getJob("testColumnMapRowMapper");
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("exeDate", LocalDate.now().plusDays(1).toString())
                .addDate("exeTime", new Date(), true)
                .toJobParameters();
        jobLauncher.run(job, jobParameters);
    }
}
