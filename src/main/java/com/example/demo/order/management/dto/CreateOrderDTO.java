package com.example.demo.order.management.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CreateOrderDTO {

//    private int ordersCustomerId;
    private LocalDateTime appointmentStart;
    private LocalDateTime appointmentEnd;
}
