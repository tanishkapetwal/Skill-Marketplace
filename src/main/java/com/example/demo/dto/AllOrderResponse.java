package com.example.demo.dto;

import com.example.demo.model.type.Status;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AllOrderResponse {
    private LocalDateTime appointmentStart;
    private LocalDateTime appointmentEnd;
    private int orderRating;
    private Status status;
    private LocalDate orderDate;
}
