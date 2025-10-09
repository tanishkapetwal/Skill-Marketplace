package com.example.demo.order.management.dto;

import com.example.demo.order.management.constant.Status;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AllOrderResponse {
    private int id;
    private int skillsListingId;
    private String skillsListingTitle;
    private String skillsListingSellerUserName;
    private LocalDateTime appointmentStart;
    private LocalDateTime appointmentEnd;
    private int orderRating;
    private Status status;
    private LocalDate orderDate;
}
