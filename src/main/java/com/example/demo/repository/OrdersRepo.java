package com.example.demo.repository;

import com.example.demo.model.Orders;
import com.example.demo.model.SkillsListing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrdersRepo extends JpaRepository<Orders, Integer> {


    List<Orders> findBySkillslisting_SellerId(int sellerId);
}
