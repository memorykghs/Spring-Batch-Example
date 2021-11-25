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
import com.batch.SpringBatchExmaple.dto.CarSpreadDto;
import com.batch.SpringBatchExmaple.entity.Car;
import com.batch.SpringBatchExmaple.repository.CarRepo;

/**
 * DB -> File
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

	/** CarRepo */
	@Autowired
	private CarRepo carRepo;

	/** 每批件數 */
	private static int FETCH_SIZE = 10;
	
	/** Mapper Field */
    private static final String[] MAPPER_FIELD = new String[] { "Manufacturer", "Type", "Spread" }; // TODO 欄位大小寫有關係?用什麼做mapping?

    /** Header */
    private final String HEADER = new StringBuilder().append("製造商").append(',').append("類別").append(',').append("價差").toString();

	/**
	 * 建立 Job
	 * 
	 * @param step
	 * @return
	 */
	@Bean
	public Job dbReaderJob(@Qualifier("Db001Step") Step step) {
		return jobBuilderFactory.get("Db001Job")
//				.preventRestart()
				.start(step)
				.listener(new Db001JobListener())
				.build();
	}

	/**
	 * 建立 Step
	 * 
	 * @param itemReader
	 * @param itemWriter
	 * @param transactionManager
	 * @return
	 */
	@Bean("Db001Step")
	public Step dbReaderStep(@Qualifier("Db001JpaReader") ItemReader<Car> itemReader, @Qualifier("Db001FileWriter") ItemWriter<CarSpreadDto> itemWriter,
			ItemProcessor<Car, CarSpreadDto> processor, JpaTransactionManager transactionManager) {

		return stepBuilderFactory.get("Db001Step")
				.transactionManager(transactionManager)
				.<Car, CarSpreadDto>chunk(FETCH_SIZE)
				.faultTolerant()
                .skip(Exception.class)
                .skipLimit(Integer.MAX_VALUE)
				.reader(itemReader)
				.processor(processor)
				.writer(itemWriter)
				.listener(new Db001StepListener())
				.listener(new Db001ReaderListener())
				.listener(new Db001WriterListener())
				.build();
	}

	/**
	 * 建立 Jpa Reader
	 * 
	 * @return
	 */
	@Bean("Db001JpaReader")
	public RepositoryItemReader<CarSpreadDto> itemReader() {

		Map<String, Direction> sortMap = new HashMap<>();
		sortMap.put("Manufacturer", Direction.ASC);
		sortMap.put("Type", Direction.ASC);

		return new RepositoryItemReaderBuilder<CarSpreadDto>()
				.name("Db001JpaReader")
				.pageSize(FETCH_SIZE)
				.repository(carRepo)
				.methodName("findAll")
				.sorts(sortMap)
				.build();
	}

	/**
	 * 建立 File Writer
	 * @return
	 */
	@Bean("Db001FileWriter")
	public FlatFileItemWriter<Car> customFlatFileWriter() {

		String fileName = new SimpleDateFormat("yyyyMMddHHmmssS").format(new Date());

		return new FlatFileItemWriterBuilder<Car>().name("Db001FileWriter")
				.encoding("UTF-8")
				.resource(new FileSystemResource("D:/" + fileName + ".csv"))
				.append(true) // 是否串接在同一個檔案後
				.delimited()
				.names(MAPPER_FIELD)
//				.shouldDeleteIfEmpty(true) // 當檔案存在且內容為空，restart時會重新生產一份
				.headerCallback(headerCallback -> headerCallback.write(HEADER)) // 使用 headerCallback 寫入表頭
				.build();
	}
}
