package com.example.demo.repository;

import com.example.demo.model.Orders;
import com.example.demo.model.SkillsListing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrdersRepo extends JpaRepository<Orders, Integer> {

    @Query("SELECT AVG(o.orderRating) FROM Orders o WHERE o.skillslisting.id = :listingId AND o.orderRating > 0")
    Double findAvgByListing(@Param("listingId") Integer listingId);

    @Query("SELECT AVG(o.orderRating) FROM Orders o JOIN o.skillslisting l WHERE l.seller.id = :sellerId AND o.orderRating > 0")
    Double findAvgBySeller(@Param("sellerId") Integer sellerId);

    List<Orders> findBySkillslisting_SellerId(int sellerId);
}

