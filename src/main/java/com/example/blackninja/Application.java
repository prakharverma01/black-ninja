package com.example.blackninja;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class Application {

	private static final Logger logg = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		logg.info("starting application....");
		SpringApplication.run(Application.class, args);
		logg.info("Application started successfully");
	}
}
