package com.batch.SpringBatchExmaple.config;

import javax.sql.DataSource;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.DefaultBatchConfigurer;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;

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
	
	/**
     * 產生 Step Transaction
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
     * @param jobRegistry
     * @return
     * @throws Exception
     */
	@Bean
	public JobRegistryBeanPostProcessor jobRegistryBeanPostProcessor(JobRegistry jobRegistry) throws Exception {
		JobRegistryBeanPostProcessor beanProcessor = new JobRegistryBeanPostProcessor();
		beanProcessor.setJobRegistry(jobRegistry);
//    	beanProcessor.afterPropertiesSet();
		return beanProcessor;
	}
}
