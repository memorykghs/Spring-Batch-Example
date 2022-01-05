package com.batch.SpringBatchExample.completionPolicy;

import org.springframework.batch.core.annotation.AfterRead;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.PeekableItemReader;
import org.springframework.batch.repeat.RepeatContext;
import org.springframework.batch.repeat.policy.CompletionPolicySupport;

public abstract class AbstractCompletionPolicy<T> extends CompletionPolicySupport {
    
    private PeekableItemReader<T> reader;
    
    private T item;
    
    private T nextItem;
    
    public void setReader(PeekableItemReader<T> reader) {
        this.reader = reader;
    }

    @Override
    public boolean isComplete(RepeatContext context) {
        return isComplete(item, nextItem);
    }
    
    @AfterRead
    public void afterRead(T item) {
        this.item = item;

        try {
            this.nextItem = this.reader.peek();
        } catch (Exception e) {
            throw new NonTransientResourceException("無法窺探下一筆資料", e);
        }
    }
    
    public abstract boolean isComplete(T item, T nextItem);
}
