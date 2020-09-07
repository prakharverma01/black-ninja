package com.example.blackninja.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {
    private String ClassType;
    private String fieldError;
    public UnauthorizedException(String message, Exception ex) {
        super(message, ex);
    }
    public UnauthorizedException(String message) {
        super(message);
    }
}
