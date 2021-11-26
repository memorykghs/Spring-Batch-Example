package com.batch.SpringBatchExmaple.batch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;

import com.batch.SpringBatchExmaple.batch.listener.Db001JobListener;
import com.batch.SpringBatchExmaple.batch.tasklet.ClearLogTasklet;

/**
 * 清除 Log 批次設定
 * 
 * @author memorykghs
 */
@Configuration
public class DbReaderJobConfig {

	/** JobBuilderFactory */
	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	/** StepBuilderFactory */
	@Autowired
	private StepBuilderFactory stepBuilderFactory;

	/**
	 * 建立 Job
	 * 
	 * @param step
	 * @return
	 */
	@Bean
	public Job dbReaderJob(@Qualifier("clearLogStep") Step step) {
		return jobBuilderFactory.get("Db001Job")
				.start(step)
				.listener(new Db001JobListener())
				.build();
	}
	
	@Bean("clearLogStep")
	public Step clearLogStep(ClearLogTasklet clearLogTasklet) {

		return stepBuilderFactory.get("Db001Step")
				.tasklet(clearLogTasklet)
				.build();
	}
}
