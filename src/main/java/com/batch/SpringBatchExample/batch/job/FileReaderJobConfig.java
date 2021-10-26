package com.batch.SpringBatchExample.batch.job;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.orm.jpa.JpaTransactionManager;

import com.batch.SpringBatchExample.batch.listener.File001JobListener;
import com.batch.SpringBatchExample.batch.listener.File001ReaderListener;
import com.batch.SpringBatchExample.batch.listener.File001StepListener;
import com.batch.SpringBatchExample.batch.listener.File001WriterListener;
import com.batch.SpringBatchExample.entity.Car;
import com.batch.SpringBatchExample.repository.CarRepo;



/**
 * File -> DB
 * @author memorykghs
 */
@Configuration
public class FileReaderJobConfig {

	/** JobBuilderFactory */
	@Autowired
	private JobBuilderFactory jobBuilderFactory;

	/** StepBuilderFactory */
	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	/** CarRepo */
	@Autowired
	private CarRepo carRepo;

	/** Mapping 欄位名稱 */
	private static final String[] MAPPER_FIELD = new String[] { "Manufacturer", "Type", "MinPrice", "Price" };

	/** 每批件數 */
	private static final int FETCH_SIZE = 1;

	/**
	 * 註冊 Job
	 * @param step
	 * @return
	 */
	@Bean("File001Job")
	public Job fileReaderJob(@Qualifier("File001Step") Step step) {
		return jobBuilderFactory.get("File001Job")
//				.preventRestart()
				.start(step)
				.listener(new File001JobListener())
				.build();
	}

	/**
	 * 註冊 Step
	 * @param itemReader
	 * @param itemWriter
	 * @param jpaTransactionManager
	 * @return
	 */
	@Bean("File001Step")
	public Step fileReaderStep(@Qualifier("File001FileReader") ItemReader<Car> itemReader, @Qualifier("File001JpaWriter") ItemWriter<Car> itemWriter,
			JpaTransactionManager jpaTransactionManager) {

		return stepBuilderFactory.get("File001Step")
				.transactionManager(jpaTransactionManager)
				.<Car, Car>chunk(FETCH_SIZE)
				.reader(itemReader)
				.faultTolerant()
//                .skip(Exception.class)
//                .skipLimit(Integer.MAX_VALUE)
				.writer(itemWriter)
				.listener(new File001StepListener())
				.listener(new File001ReaderListener())
				.listener(new File001WriterListener())
				.build();
	}

	/**
	 * 建立 FileReader
	 * @return
	 */
	@Bean("File001FileReader")
	public ItemReader<Car> getItemReader(@Value("${filePath}") String filePath) {
		return new FlatFileItemReaderBuilder<Car>().name("File001FileReader")
				.encoding("UTF-8")
				.resource(new FileSystemResource("D:/cars.csv"))
//				.resource(new ClassPathResource("csv/Cars.csv"))
//				.resource(new PathResource("C://Users/user/Desktop/Cars.csv"))
				.linesToSkip(1)
//				.delimited()
//				.names(MAPPER_FIELD)
//				.fieldSetMapper(new BeanWrapperFieldSetMapper<Car>())
				 .lineMapper(getCarLineMapper())
				.build();
	}

	/**
	 * 建立 FileReader mapping 規則
	 * @return
	 */
	private LineMapper<Car> getCarLineMapper() {
		DefaultLineMapper<Car> bookInfoLineMapper = new DefaultLineMapper<>();

		// 1. 設定每一筆資料的欄位拆分規則，預設以逗號拆分
		DelimitedLineTokenizer tokenizer = new DelimitedLineTokenizer();
		tokenizer.setNames(MAPPER_FIELD);

		// 2. 設定資料轉換到程式面的規則
		BeanWrapperFieldSetMapper<Car> fieldSetMapper = new
		BeanWrapperFieldSetMapper<>();
		fieldSetMapper.setTargetType(Car.class);

		bookInfoLineMapper.setLineTokenizer(tokenizer);
		bookInfoLineMapper.setFieldSetMapper(fieldSetMapper);
		return bookInfoLineMapper;
	}
	
	/**
	 * 建立 Jpa ItemWriter
	 * @return
	 */
	@Bean("File001JpaWriter")
	public ItemWriter<Car> getItemWriter(){
		
		return new RepositoryItemWriterBuilder<Car>()
				.repository(carRepo)
				.methodName("save")
				.build();
	}
}
