package com.batch.SpringBatchExmaple.batch.processor;

import java.math.BigDecimal;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import com.batch.SpringBatchExmaple.dto.CarsDto;
import com.batch.SpringBatchExmaple.entity.Cars;
import com.batch.SpringBatchExmaple.exception.DataNotFoundException;
import com.batch.SpringBatchExmaple.exception.RangeLimitExcpetion;

/**
 * Item Processor
 * 
 * @author memorykghs
 */
@Component
public class DBItemProcessor implements ItemProcessor<Cars, CarsDto> {

	private static final BigDecimal defaultSpread = new BigDecimal("50");

	@Override
	public CarsDto process(Cars item) throws Exception {

		// 計算每一廠牌汽車底價及售價價差
		CarsDto carsDto = new CarsDto();
		carsDto.setManufacturer(item.getManufacturer());
		carsDto.setType(item.getType());

		BigDecimal spread = item.getPrice().subtract(item.getMinPrice());
		carsDto.setSpread(spread);

		// 判斷價差是否過大
		if (defaultSpread.compareTo(spread) == -1) {
			throw new RangeLimitExcpetion();

		} else if (defaultSpread.compareTo(spread) == 0) {
			throw new DataNotFoundException();
		}

		return carsDto;
	}
}
