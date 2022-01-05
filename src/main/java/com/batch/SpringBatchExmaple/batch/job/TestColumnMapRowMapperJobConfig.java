package com.batch.SpringBatchExample.batch.job;

import java.util.Map;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.core.ColumnMapRowMapper;

import com.batch.SpringBatchExample.batch.process.TestColumnMapRowMapperProcess;

@Configuration
public class TestColumnMapRowMapperJobConfig {
    
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    
    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private TestColumnMapRowMapperProcess testColumnMapRowMapperProcess;

    @Bean
    public Job testColumnMapRowMapper() {
        return jobBuilderFactory.get("testColumnMapRowMapper")
                .start(testColumnMapRowMapperStep())
                .incrementer(new RunIdIncrementer())
                .build();
    }
    
    @Bean
    public Step testColumnMapRowMapperStep() {
        return stepBuilderFactory.get("testColumnMapRowMapperStep")
                .<Map<String, Object>, Map<String, Object>>chunk(1)
                .reader(testColumnMapRowMapperReader())
                .processor(testColumnMapRowMapperProcess)
                .writer(testColumnMapRowMapperWriter())
                .build();
    }
    
    @Bean
    @StepScope  // 若多個實例同時進行，沒加StepScope會拋例外
    public JdbcCursorItemReader<Map<String, Object>> testColumnMapRowMapperReader() {
        return new JdbcCursorItemReaderBuilder<Map<String, Object>>()
                .name("testColumnMapRowMapperReader")
                .dataSource(dataSource)
                .sql("select SYSTEM, PROGRAM_ID, PROGRAM_NM, UPD_TM from BT_IN")
                .rowMapper(new ColumnMapRowMapper())
                .build();
    }
    
    @Bean
    public FlatFileItemWriter<Map<String, Object>> testColumnMapRowMapperWriter() {
        return new FlatFileItemWriterBuilder<Map<String, Object>>()
                .name("testColumnMapRowMapperWriter")
                .encoding("UTF-8")
                .resource(new FileSystemResource("D:/test.dat"))
                .delimited()
                .delimiter(",")
                .fieldExtractor(new FieldExtractor<Map<String,Object>>() {
                    
                    @Override
                    public Object[] extract(Map<String, Object> item) {
                        return item.values().toArray();
                    }
                })
                .build();
    }
}
