package com.example.blackninja;

import com.example.blackninja.model.Users;
import com.example.blackninja.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@SpringBootApplication
public class Application {

	@Autowired
	private UserRepository userRepository;

	private static final Logger logg = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		logg.info("starting application....");
		SpringApplication.run(Application.class, args);
		logg.info("Application started successfully");
	}

	@GetMapping("/allUsers")
	public ResponseEntity<Users> fun() {
		List<Users> user = userRepository.findAll();
		return new ResponseEntity(user, HttpStatus.OK);
	}
}
