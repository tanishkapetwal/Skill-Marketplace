package com.example.demo.repository;

import com.example.demo.model.Seller;
import com.example.demo.model.SkillsListing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SkillsListingRepo extends JpaRepository<SkillsListing, Integer> {

    List<SkillsListing> findBySellerId(Integer sellerId);   //sellerId= Spring automatically uses "Seller_Obj.id"
}

