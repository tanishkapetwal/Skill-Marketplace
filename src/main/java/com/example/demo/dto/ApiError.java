package com.example.demo.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Data
public class ApiError {

    private String message;
    private HttpStatus httpStatus;
    private LocalDateTime timestamp;

    public ApiError(String message, HttpStatus httpStatus) {
        this.message = message;
        this.httpStatus = httpStatus;
        this.timestamp = LocalDateTime.now();
    }
}

