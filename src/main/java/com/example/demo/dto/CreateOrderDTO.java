package com.example.demo.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateOrderDTO {

    private LocalDateTime appointmentStart;
    private LocalDateTime appointmentEnd;
}
