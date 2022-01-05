package com.batch.SpringBatchExample.batch.job;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.item.support.SingleItemPeekableItemReader;
import org.springframework.batch.item.support.builder.SingleItemPeekableItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.batch.SpringBatchExample.completionPolicy.TestGroupItemCompletionPolicy;
import com.batch.SpringBatchExample.dto.CardDto;

@Configuration
public class TestGroupItemJobConfig {
    
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    
    @Autowired
    private DataSource dataSource;

    @Bean
    public Job testGroupItem() {
        return jobBuilderFactory.get("testGroupItem")
                .start(testGroupItemStep())
                .build();
    }
    
    @Bean
    public Step testGroupItemStep() {        
        TestGroupItemCompletionPolicy completionPolicy = new TestGroupItemCompletionPolicy();
        completionPolicy.setReader(testGroupItemReader());

        return stepBuilderFactory.get("testGroupItemStep")
                .<CardDto, CardDto>chunk(completionPolicy)
                .reader(testGroupItemReader())
                .writer(new ItemWriter<CardDto>() {
                    
                    @Override
                    public void write(List<? extends CardDto> items) throws Exception {    
                        items.stream().forEach(System.err::println);
                        System.err.println("======分隔線======");
                    }
                })
                .listener(completionPolicy)     // 因有使用到@AfterRead，所以也需指定為listener
                .build();
    }
    
    @Bean
    public SingleItemPeekableItemReader<CardDto> testGroupItemReader() {
        JdbcCursorItemReader<CardDto> jdbcCursorReader =  new JdbcCursorItemReaderBuilder<CardDto>()
                .name("readYfuCardReader")
                .dataSource(dataSource)
                .sql("select * from STUDENT.YFU_CARD order by TYPE desc")
                .beanRowMapper(CardDto.class)
                .build();
        
        return new SingleItemPeekableItemReaderBuilder<CardDto>()
                .delegate(jdbcCursorReader).build();
    }
}
