package com.batch.SpringBatchExmaple.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemReadListener;

import com.batch.SpringBatchExmaple.dto.CarsDto;

/**
 * Db001 ReaderListener
 * @author memeorykghs
 */
public class Db001ReaderListener implements ItemReadListener<CarsDto> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Db001ReaderListener.class);

    @Override
    public void beforeRead() {
    	LOGGER.info("Db001Reader: 讀取資料開始");

    }

    @Override
    public void afterRead(CarsDto item) {
    	System.out.println("==========> " + item.getManufacturer());

    }

    @Override
    public void onReadError(Exception ex) {
        LOGGER.error("Db001Reader: 讀取資料失敗", ex);
    }
}