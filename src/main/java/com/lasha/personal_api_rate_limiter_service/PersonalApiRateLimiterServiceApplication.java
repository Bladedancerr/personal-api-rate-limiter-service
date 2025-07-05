package com.lasha.personal_api_rate_limiter_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PersonalApiRateLimiterServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(PersonalApiRateLimiterServiceApplication.class, args);
	}

}
