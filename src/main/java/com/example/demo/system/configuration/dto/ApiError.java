package com.example.demo.system.configuration.dto;

import lombok.Data;
import org.springframework.http.HttpStatus;
import java.time.LocalDateTime;

@Data
public class ApiError {

    private String message;
    private HttpStatus httpStatus;
    private int status;
    private LocalDateTime timestamp;

    public ApiError(String message, int status) {
        this.message = message;
        this.status = status;
        this.timestamp = LocalDateTime.now();
    }
}

