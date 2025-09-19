package com.example.demo.dto;


import com.example.demo.model.type.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SellerOrdersDTO {
    private int id;   // orderId
    private Status ordersStatus;
    private LocalDate ordersOrderDate;
    private float skillsListingPrice;  // spring automatically assigns price of SkillsListing model
    private String ordersCustomerUserName;
    private String ordersSkillsListingSkillsName;
}
