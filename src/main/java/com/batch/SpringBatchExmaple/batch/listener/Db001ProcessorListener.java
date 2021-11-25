package com.batch.SpringBatchExmaple.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ItemProcessListener;

import com.batch.SpringBatchExmaple.dto.CarSpreadDto;
import com.batch.SpringBatchExmaple.entity.Car;

/**
 * Item Processor Listnener
 * @author memorykghs
 */
public class Db001ProcessorListener implements ItemProcessListener<Car, CarSpreadDto>{
    
    private static final Logger LOGGER = LoggerFactory.getLogger(Db001ProcessorListener.class);

    @Override
    public void beforeProcess(Car item) {
        LOGGER.info("Manufacturer = {}", item.getManufacturer());
        LOGGER.info("Type = {}", item.getType());
        
    }

    @Override
    public void afterProcess(Car item, CarSpreadDto result) {
        LOGGER.info("Spread = {}", result.getSpread());
        
    }

    @Override
    public void onProcessError(Car item, Exception e) {
        LOGGER.info("Db001Processor, error item = {}, {}", item.getManufacturer(), item.getType());
        LOGGER.info("errMsg = {}", e.getMessage());
    }
}
