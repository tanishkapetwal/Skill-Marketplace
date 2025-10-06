package com.example.demo.customer.managment.model;

import com.example.demo.system.configuration.model.User;
import com.example.demo.order.management.model.Orders;
import jakarta.persistence.*;
import lombok.*;


import java.util.List;


@Data
@AllArgsConstructor
@Entity
@Builder
public class Customer {

    public Customer() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL)
    private List<Orders> order;

}
