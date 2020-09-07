package com.example.blackninja.api;

import com.example.blackninja.dtos.request.LoginRequest;
import com.example.blackninja.dtos.request.UserRequest;
import com.example.blackninja.dtos.response.GenericResponse;
import com.example.blackninja.dtos.response.UserRegistrationResponse;
import com.example.blackninja.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api/user")
public class UserAuthController {

    private UserService userService;

    @Autowired
    public UserAuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<GenericResponse<UserRegistrationResponse>> registerUser(@RequestBody UserRequest userRequest) {
        UserRegistrationResponse response = userService.userRegistration(userRequest);
        GenericResponse<UserRegistrationResponse> genericResponse = new GenericResponse(
                response,
                "USER REGISTRATION",
                HttpStatus.CREATED,
                HttpStatus.CREATED.value(),
                Collections.emptyList()
        );
        return new ResponseEntity(genericResponse, HttpStatus.CREATED);
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest) {
        String response = userService.login(loginRequest);
        return new ResponseEntity(response, HttpStatus.OK);
    }
}
