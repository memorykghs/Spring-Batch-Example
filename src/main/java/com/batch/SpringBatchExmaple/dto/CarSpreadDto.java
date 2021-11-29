package com.batch.SpringBatchExmaple.dto;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.Data;

@Data
public class CarSpreadDto implements Serializable{
	private static final long serialVersionUID = 1L;

	private String manufacturer;

	private String type;

	private BigDecimal spread;
}
