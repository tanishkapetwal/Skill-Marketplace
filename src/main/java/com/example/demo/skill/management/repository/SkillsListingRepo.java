package com.example.demo.skill.management.repository;

import com.example.demo.skill.management.model.SkillsListing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SkillsListingRepo extends JpaRepository<SkillsListing, Integer> {

    List<SkillsListing> findBySellerId(Integer sellerId);   //sellerId= Spring automatically uses "Seller_Obj.id"
}

