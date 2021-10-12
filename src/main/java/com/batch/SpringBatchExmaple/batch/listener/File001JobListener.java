package com.batch.SpringBatchExmaple.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;


/**
 * File001 JobListener
 * @author memorykghs
 */
public class File001JobListener implements JobExecutionListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(File001JobListener.class);

	@Override
	public void beforeJob(JobExecution jobExecution) {
		LOGGER.info("File001Job: 批次開始");
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		LOGGER.info("File001Job: 批次結束");
	}
}
