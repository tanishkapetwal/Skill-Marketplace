package com.example.demo.repository;

import com.example.demo.model.Orders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface OrdersRepo extends JpaRepository<Orders, Integer> {

    @Query("SELECT AVG(o.orderRating) FROM Orders o WHERE o.skillslisting.id = :listingId AND o.orderRating > 0")
    Double findAvgByListing(@Param("listingId") int listingId);

    @Query("SELECT AVG(o.orderRating) FROM Orders o JOIN o.skillslisting l WHERE l.seller.id = :sellerId AND o.orderRating > 0")
    Double findAvgBySeller(@Param("sellerId") int sellerId);

    List<Orders> findBySkillslisting_SellerId(int sellerId);

    Page<Orders> findByCustomerId(Pageable pageable , int id);

}

