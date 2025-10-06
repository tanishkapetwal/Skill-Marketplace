package com.example.demo.seller.management.repository;

import com.example.demo.seller.management.model.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SellerRepo extends JpaRepository<Seller, Integer>{
    Optional<Seller> findByUserId(int user_id);

}
