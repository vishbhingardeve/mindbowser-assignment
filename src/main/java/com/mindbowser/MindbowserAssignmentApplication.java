package com.mindbowser;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class MindbowserAssignmentApplication {
	public static void main(String[] args) {
		log.info("Enter: Method main : Request Arguments: {}", "Start application configuration");
		SpringApplication.run(MindbowserAssignmentApplication.class, args);
		log.info("Exit : Method main : Response Arguments: {}","Success configuration");
	}
}
