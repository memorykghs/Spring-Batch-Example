package com.batch.SpringBatchExmaple.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;

/**
 * Db001 JobListener
 * @author memorykghs
 */
public class Db001JobListener implements JobExecutionListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(Db001JobListener.class);
	
	@Override
	public void beforeJob(JobExecution jobExecution) {
		LOGGER.info("Db001Job: 批次開始");
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		LOGGER.info("Db001Job: 批次結束");
	}

}
