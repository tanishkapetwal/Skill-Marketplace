package com.example.demo.order.management.dto;


import com.example.demo.order.management.constant.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SellerRequestedOrdersDTO {
    private int id;   // orderId
    private Status ordersStatus;
    private LocalDate ordersOrderDate;
    private float skillsListingPrice;
    private String ordersCustomerUserName;
    private String ordersSkillsListingSkillsName;
}
