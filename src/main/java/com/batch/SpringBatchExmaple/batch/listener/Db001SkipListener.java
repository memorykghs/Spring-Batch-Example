package com.batch.SpringBatchExmaple.batch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.SkipListener;

import com.batch.SpringBatchExmaple.dto.CarsDto;
import com.batch.SpringBatchExmaple.entity.Cars;

/**
 * Skip Listener
 * 
 * @author memorykghs
 */
public class Db001SkipListener implements SkipListener<Cars, CarsDto> {

	private static final Logger LOGGER = LoggerFactory.getLogger(Db001SkipListener.class);

	@Override
	public void onSkipInRead(Throwable t) {
		LOGGER.error("Skip read message: {}", t.getMessage());

	}

	@Override
	public void onSkipInWrite(CarsDto item, Throwable t) {
		LOGGER.info("Skip item: {}", item.getManufacturer(), item.getType());
		LOGGER.error("Skip write message: {}", t.getMessage());

	}

	@Override
	public void onSkipInProcess(Cars item, Throwable t) {
		LOGGER.error("Skip on process message: {}", item.getManufacturer(), item.getType());
	}
}
