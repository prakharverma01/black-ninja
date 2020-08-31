package com.example.blackninja.dtos.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter @Setter @NoArgsConstructor
public class GenericResponse<T> {

    private T data;
    private String meta;
    private HttpStatus status;
    private int statusCode;
    private List<Errors> errorsList;

    public GenericResponse(T data, String meta, HttpStatus status, int statusCode, List<Errors> errorsList) {
        this.data = data;
        this.meta = meta;
        this.status = status;
        this.statusCode = statusCode;
        this.errorsList = errorsList;
    }
}
