package com.batch.SpringBatchExmaple;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class SpringBatchExmapleApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBatchExmapleApplication.class, args);
	}

}
