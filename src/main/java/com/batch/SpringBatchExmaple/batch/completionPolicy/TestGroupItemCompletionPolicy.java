package com.batch.SpringBatchExample.completionPolicy;

import com.batch.SpringBatchExample.dto.CardDto;

public class TestGroupItemCompletionPolicy extends AbstractCompletionPolicy<CardDto> {

    @Override
    public boolean isComplete(CardDto item, CardDto nextItem) {
        return nextItem != null && !item.getType().equals(nextItem.getType());
    }
}
