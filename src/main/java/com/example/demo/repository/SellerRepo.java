package com.example.demo.repository;

import com.example.demo.model.Customer;
import com.example.demo.model.Seller;
import com.example.demo.model.SkillsListing;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SellerRepo extends JpaRepository<Seller, Integer>{
    Optional<Seller> findByUserId(int user_id);

}
