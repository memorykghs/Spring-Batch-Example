package com.batch.SpringBatchExmaple;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
@EnableBatchProcessing
public class SpringBatchExmapleApplication {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpringBatchExmapleApplication.class);

	public static void main(String[] args) throws NoSuchJobException, JobExecutionAlreadyRunningException,
			JobRestartException, JobInstanceAlreadyCompleteException, JobParametersInvalidException {
		try {
			String jobName = args[0];

			SpringApplication.run(SpringBatchExmapleApplication.class, args);
			// ConfigurableApplicationContext context = SpringApplication.run(SpringBatchExmapleApplication.class, args);
			Job job = context.getBean(JobRegistry.class).getJob(jobName);
			context.getBean(JobLauncher.class).run(job, createJobParams());

		} catch (Exception e) {
			LOGGER.error("springBatchPractice執行失敗", e);
		}
	}

	private static JobParameters createJobParams() {

		JobParametersBuilder builder = new JobParametersBuilder();
		builder.addDate("date", new Date());

		return builder.toJobParameters();
	}
}
