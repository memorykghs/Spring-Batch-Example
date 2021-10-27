package com.batch.SpringBatchExmaple.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.Data;

@Embeddable
@Data
public class CarsPK implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "MANUFACTURER")
	private String manufacturer;

	@Column(name = "TYPE")
	private String type;

	public String getManufacturer() {
		return this.manufacturer;
	}
}
