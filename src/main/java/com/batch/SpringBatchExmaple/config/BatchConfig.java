package com.batch.SpringBatchExmaple.config;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.explore.support.JobExplorerFactoryBean;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobOperator;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.orm.jpa.JpaTransactionManager;

/**
 * Batch Config
 * 
 * @author memorykghs
 */
@Configuration
public class BatchConfig {

	/**
	 * 產生 Step Transaction
	 * 
	 * @return
	 */
	@Bean
	public JpaTransactionManager jpaTransactionManager(DataSource dataSource) {
		final JpaTransactionManager transactionManager = new JpaTransactionManager();
		transactionManager.setDataSource(dataSource);

		return transactionManager;
	}

	/**
	 * 使用 JobRegistry 註冊 Job
	 * 
	 * @param jobRegistry
	 * @return
	 * @throws Exception
	 */
	@Bean
	public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) throws Exception {
		JobRegistryBeanPostProcessor beanProcessor = new JobRegistryBeanPostProcessor();
		beanProcessor.setJobRegistry(jobRegistry);
		beanProcessor.afterPropertiesSet();

		return beanProcessor;
	}

	/**
	 * <pre>
	 * 建立 JobLauncher
	 * for Web Container
	 * </pre>
	 * 
	 * @param jobRepository
	 * @return
	 * @throws Exception
	 */
	@Bean
	public SimpleJobLauncher jobLauncher(JobRepository jobRepository) throws Exception {
		SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
		jobLauncher.setJobRepository(jobRepository);
		jobLauncher.setTaskExecutor(new SimpleAsyncTaskExecutor()); // web container 一般會設定非同步
		jobLauncher.afterPropertiesSet();

		return jobLauncher;
	}

	/**
	 * <pre>
	 * 建立 JobExploer
	 * for Web Container
	 * </pre>
	 * 
	 * @param dataSource
	 * @return
	 * @throws Exception
	 */
	@Bean
	public JobExplorer jobExplorer(DataSource dataSource) throws Exception {
		JobExplorerFactoryBean factoryBean = new JobExplorerFactoryBean();
		factoryBean.setDataSource(dataSource);
		// factoryBean.setTablePrefix("Ashley");

		return factoryBean.getObject();
	}

	/**
	 * <pre>
	 * 建立 JobOperator
	 * for Web Container
	 * </pre>
	 * 
	 * @param jobExplorer
	 * @param jobRepository
	 * @param jobRegistry
	 * @param jobLauncher
	 * @return
	 */
	@Bean
	public SimpleJobOperator jobOperator(JobExplorer jobExplorer, JobRepository jobRepository, JobRegistry jobRegistry,
			JobLauncher jobLauncher) {
		SimpleJobOperator jobOperator = new SimpleJobOperator();
		jobOperator.setJobExplorer(jobExplorer);
		jobOperator.setJobRepository(jobRepository);
		jobOperator.setJobRegistry(jobRegistry);
		jobOperator.setJobLauncher(jobLauncher);

		return jobOperator;
	}
}
