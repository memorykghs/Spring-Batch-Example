package com.batch.SpringBatchExmaple.batch.listener;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemWriteListener;

import com.batch.SpringBatchExmaple.dto.CarSpreadDto;


/**
 * Db001 WriterListener
 * @author memorykghs
 */
public class Db001WriterListener implements ItemWriteListener<CarSpreadDto> {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Db001WriterListener.class);

    @Override
    public void beforeWrite(List<? extends CarSpreadDto> items) {
        LOGGER.info("寫入資料開始");
    }

    @Override
    public void afterWrite(List<? extends CarSpreadDto> items) {
        LOGGER.info("寫入資料結束");
    }

    @Override
    public void onWriteError(Exception ex, List<? extends CarSpreadDto> items) {
        LOGGER.error("Db001Writer: 寫入資料失敗", ex);
    }

}