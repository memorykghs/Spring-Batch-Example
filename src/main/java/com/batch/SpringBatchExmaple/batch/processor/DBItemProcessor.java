package com.batch.SpringBatchExmaple.batch.processor;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.batch.SpringBatchExmaple.dto.CarSpreadDto;
import com.batch.SpringBatchExmaple.entity.Car;

/**
 * Item Processor
 * 
 * @author memorykghs
 */
@Component
public class DBItemProcessor implements ItemProcessor<Car, CarSpreadDto> {

	@Override
	public CarSpreadDto process(Car item) throws Exception {

		// 計算每一廠牌汽車底價及售價價差
		CarSpreadDto carsDto = new CarSpreadDto();
		carsDto.setManufacturer(item.getManufacturer());
		carsDto.setType(item.getType());
		carsDto.setSpread(item.getPrice().subtract(item.getMinPrice()));

		return carsDto;
	}

}
