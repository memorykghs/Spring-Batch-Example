package com.batch.SpringBatchExmaple.batch.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.batch.SpringBatchExmaple.dto.CarsDto;
import com.batch.SpringBatchExmaple.entity.Cars;

/**
 * Item Processor
 * 
 * @author memorykghs
 */
@Component
public class DBItemProcessor implements ItemProcessor<Cars, CarsDto> {

	@Override
	public CarsDto process(Cars item) throws Exception {

		// 計算每一廠牌汽車底價及售價價差
		CarsDto carsDto = new CarsDto();
		carsDto.setManufacturer(item.getManufacturer());
		carsDto.setType(item.getType());
		carsDto.setSpread(item.getPrice().subtract(item.getMinPrice()));

		return carsDto;
	}

}
