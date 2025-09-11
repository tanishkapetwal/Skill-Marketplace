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

    private Status ordersStatus;
    private LocalDate ordersOrderDate;

    private String ordersCustomerUserName;
    private String ordersSkillsListingSkillsName;
}
