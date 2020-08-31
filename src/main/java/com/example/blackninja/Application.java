package com.example.blackninja;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableAutoConfiguration
public class Application {

	private static final Logger logg = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		logg.info("starting application....");
		SpringApplication.run(Application.class, args);
		logg.info("Application started successfully");
	}
}
