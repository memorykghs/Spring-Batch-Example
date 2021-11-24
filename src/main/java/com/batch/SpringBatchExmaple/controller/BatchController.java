package com.batch.SpringBatchExmaple.controller;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.launch.JobInstanceAlreadyExistsException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.JobParametersNotFoundException;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Spring Batch Examples")
@RestController
public class BatchController {

	/** LOG */
	private static final Logger LOGGER = LoggerFactory.getLogger(BatchController.class);

	/** JobOperator */
	@Autowired
	private JobOperator jobOperator;

	@Autowired
	private JobRegistry jobRegistry;

	@Autowired
	private JobLauncher jobLauncher;

	/**
	 * call by web conatiner
	 * 
	 * @throws JobExecutionAlreadyRunningException
	 * @throws JobRestartException
	 * @throws JobInstanceAlreadyCompleteException
	 * @throws JobParametersInvalidException
	 */
	@ApiOperation(value = "執行讀DB批次 (JobLauncher)")
	@RequestMapping(value = "/dbReader001Job", method = RequestMethod.POST)
	public String doDbReader001Job() {

		try {

			JobParametersBuilder builder = new JobParametersBuilder();
			builder.addString("sameParameter", "string");

//			jobLauncher.run(jobRegistry.getJob("Db001Job"), new JobParameters());
			jobLauncher.run(jobRegistry.getJob("Db001Job"), builder.toJobParameters());
//			jobLauncher.run(jobRegistry.getJob("Db001Job"), createJobParams("Db001Job"));

		} catch (JobExecutionAlreadyRunningException jobExecutionAlreadyRunningException) {
			LOGGER.info("Job execution is already running.");

		} catch (JobRestartException jobRestartException) {
			LOGGER.info("Job restart exception happens.");

		} catch (JobInstanceAlreadyCompleteException jobInstanceAlreadyCompleteException) {
			LOGGER.info("Job instance is already completed.");

		} catch (JobParametersInvalidException jobParametersInvalidException) {
			LOGGER.info("Job parameters invalid exception");

		} catch (BeansException beansException) {
			LOGGER.info("Bean is not found.");

		} catch (NoSuchJobException e) {
			e.printStackTrace();
		}

		return "finished";
	}

	/**
	 * 使用 JobOperator
	 * 
	 * @return
	 */
	@ApiOperation(value = "執行讀DB批次 (JobOperator)")
	@RequestMapping(value = "/dbReader001Job2", method = RequestMethod.POST)
	public String doDbReader001Job2() {

		String summary = null;

		try {
//			long jobExecutionId = jobOperator.start("Db001Job", "1"); // 取得 JobExecutionId
			long jobExecutionId = jobOperator.startNextInstance("Db001Job");
			summary = jobOperator.getSummary(jobExecutionId);

		} catch (NoSuchJobException | JobParametersInvalidException e) {
			e.printStackTrace();
		} catch (NoSuchJobExecutionException e) {
			e.printStackTrace();
		} catch (JobParametersNotFoundException e) {
			e.printStackTrace();
		} catch (JobRestartException e) {
			e.printStackTrace();
		} catch (JobExecutionAlreadyRunningException e) {
			e.printStackTrace();
		} catch (JobInstanceAlreadyCompleteException e) {
			e.printStackTrace();
		} catch (UnexpectedJobExecutionException e) {
			e.printStackTrace();
		}

		return summary;
	}

	/**
	 * 產生JobParameter
	 * 
	 * @return
	 */
	private JobParameters createJobParams(String jobName) {

		JobParametersBuilder builder = new JobParametersBuilder();
		builder.addDate("executeTime", Timestamp.valueOf(LocalDateTime.now()));

		return builder.toJobParameters();
	}
}
