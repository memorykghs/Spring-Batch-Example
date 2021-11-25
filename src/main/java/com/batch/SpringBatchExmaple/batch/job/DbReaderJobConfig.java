package com.batch.SpringBatchExmaple.batch.job;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.orm.jpa.JpaTransactionManager;

import com.batch.SpringBatchExmaple.batch.listener.Db001JobListener;
import com.batch.SpringBatchExmaple.batch.listener.Db001ReaderListener;
import com.batch.SpringBatchExmaple.batch.listener.Db001StepListener;
import com.batch.SpringBatchExmaple.batch.listener.Db001WriterListener;
import com.batch.SpringBatchExmaple.batch.tasklet.ClearLogTasklet;
import com.batch.SpringBatchExmaple.dto.CarsDto;
import com.batch.SpringBatchExmaple.entity.Cars;
import com.batch.SpringBatchExmaple.repository.CarsRepo;

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
	public Step clearLogStep(JpaTransactionManager transactionManager) {

		return stepBuilderFactory.get("Db001Step")
				.transactionManager(transactionManager)
				.tasklet(new ClearLogTasklet())
				.build();
	}
}
