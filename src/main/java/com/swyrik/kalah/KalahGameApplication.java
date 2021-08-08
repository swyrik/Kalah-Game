package com.swyrik.kalah;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;

@OpenAPIDefinition
@SpringBootApplication
public class KalahGameApplication{

	public static void main(String[] args) {
		SpringApplication.run(KalahGameApplication.class, args);
	}

} 	
