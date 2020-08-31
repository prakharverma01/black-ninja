package com.example.blackninja.dtos.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter @Setter @NoArgsConstructor
public class Errors {

    private String message; private HttpStatus status; private String contextPath;

    public Errors(String message, HttpStatus status, String contextPath) {
        this.message = message; this.status = status; this.contextPath = contextPath;
    }
}
