package com.batch.SpringBatchExmaple.config;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.context.annotation.Configuration;

/**
 * Batch Config
 * @author memorykghs
 */
@Configuration
//@EnableBatchProcessing(modular = true)
public class BatchConfig extends DefaultBatchConfigurer {
    @Override
    public void setDataSource(DataSource dataSource) {
        // 讓Spring Batch自動產生的table不寫入DB
    }

}
