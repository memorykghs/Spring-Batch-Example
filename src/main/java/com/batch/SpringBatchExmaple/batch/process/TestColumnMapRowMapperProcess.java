package com.batch.SpringBatchExample.batch.process;

import java.util.Map;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

@StepScope
@Component
public class TestColumnMapRowMapperProcess implements ItemProcessor<Map<String, Object>, Map<String, Object>> {
    
    /** 確認每個不同Job實體都有自己的JobParameters */
    @Value("#{jobParameters[exeDate]}")
    private String exeDate;
    
    /** 確認每個StepScope都有自己的靜態變數 */
    private int count = 0;

    @Override
    @Retryable(value = Exception.class, maxAttempts = 3, backoff = @Backoff(delay = 3000))
    public Map<String, Object> process(Map<String, Object> item) throws Exception {
        if (true) {
            System.err.println(exeDate + ": " + ++count);
            throw new Exception("想過? 給過路費阿");
        }
        return item;
    }
}
