package com.example.demo.repository;

import com.example.demo.model.SkillsListing;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SkillsListingRepo extends JpaRepository<SkillsListing, Integer> {

    List<SkillsListing> findBySellerId(Integer sellerId);   //sellerId= Spring automatically uses "Seller_Obj.id"
    Page<SkillsListing> findAll(Pageable pageable);
}

