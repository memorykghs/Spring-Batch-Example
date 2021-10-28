package com.batch.SpringBatchExmaple.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ChunkListener;
import org.springframework.batch.core.scope.context.ChunkContext;

public class Db001ChunkListener implements ChunkListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(Db001ChunkListener.class);

    @Override
    public void beforeChunk(ChunkContext context) {
        LOGGER.info("chunk {} 開始", context.getStepContext().getId());
    }

    @Override
    public void afterChunk(ChunkContext context) {
        LOGGER.info("chunk {} 結束", context.getStepContext().getId());
        count++;

    }

    @Override
    public void afterChunkError(ChunkContext context) {
        // TODO Auto-generated method stub

    }

}
