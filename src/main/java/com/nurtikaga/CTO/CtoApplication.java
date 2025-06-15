package com.nurtikaga.CTO;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CtoApplication {
	public static void main(String[] args) {
		SpringApplication.run(CtoApplication.class, args);
	}
}

