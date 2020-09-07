package com.example.blackninja.exceptions.advice;

import com.example.blackninja.Application;
import com.example.blackninja.dtos.response.Errors;
import com.example.blackninja.dtos.response.GenericResponse;
import com.example.blackninja.exceptions.BadRequestException;
import com.example.blackninja.exceptions.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@ControllerAdvice
@RestController
@ResponseBody
public class CustomizedResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(CustomizedResponseEntityExceptionHandler.class);

    @ExceptionHandler(Exception.class)
    public ResponseEntity<GenericResponse<Object>> handleAllExceptions(Exception exception, WebRequest webRequest) {
        log.error("Error occurred =>", exception);
        Errors err = new Errors(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, webRequest.getContextPath());
        List<Errors> errList = new ArrayList<>();
        errList.add(err);
        GenericResponse<Object> genericResponse = new GenericResponse(null, "INTERNAL_SERVER_ERROR", HttpStatus.INTERNAL_SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR.value(), errList);
        return new ResponseEntity(genericResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<GenericResponse<Object>> handleAllBadRequestExceptions(Exception exception, WebRequest webRequest) {
        log.error("BadRequestException occurred =>", exception);
        Errors err = new Errors(exception.getMessage(), HttpStatus.BAD_REQUEST, webRequest.getContextPath());
        List<Errors> errList = new ArrayList<>();
        errList.add(err);
        GenericResponse<Object> genericResponse = new GenericResponse(null, "BAD_REQUEST_EXCEPTION", HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(), errList);
        return new ResponseEntity(genericResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<GenericResponse<Object>> handleAllUnauthorizedExceptions(Exception exception, WebRequest webRequest) {
        log.error("UnauthorizedException occurred =>", exception);
        Errors err = new Errors(exception.getMessage(), HttpStatus.UNAUTHORIZED, webRequest.getContextPath());
        List<Errors> errList = new ArrayList<>();
        errList.add(err);

        GenericResponse<Object> genericResponse = new GenericResponse("Unauthorized", "UNAUTHORIZED_EXCEPTION", HttpStatus.UNAUTHORIZED, HttpStatus.UNAUTHORIZED.value(), errList);
        return new ResponseEntity(genericResponse, HttpStatus.UNAUTHORIZED);
    }
}
