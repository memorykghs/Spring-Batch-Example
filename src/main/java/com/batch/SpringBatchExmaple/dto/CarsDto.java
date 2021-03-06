package com.batch.SpringBatchExmaple.dto;

import java.io.Serializable;

import lombok.Data;

/**
 * Cars 檔案對應 dto
 * @author memorykghs
 */
@Data
public class CarsDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private String manufacturer;

	private String type;

	private String minPrice;

	private String price;

}