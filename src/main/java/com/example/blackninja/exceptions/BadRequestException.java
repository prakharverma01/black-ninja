package com.example.blackninja.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BadRequestException extends RuntimeException {
    private String ClassType;
    private String fieldError;
    public BadRequestException(String message, Exception ex) {
        super(message, ex);
    }
    public  BadRequestException(String message) {
        super(message);
    }
}
