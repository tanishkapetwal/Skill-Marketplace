package com.example.demo.service;

import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.SkillsListing;
import com.example.demo.repository.SkillsListingRepo;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class SkillsListingService {

    private final SkillsListingRepo skillsListingRepo;

    public SkillsListingService(SkillsListingRepo skillsListingRepo) {
        this.skillsListingRepo = skillsListingRepo;
    }

    public List<SkillsListing> getAllListings() {
        return skillsListingRepo.findAll();
    }

    public List<SkillsListing> getListingById(int id) {
        return Collections.singletonList(skillsListingRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("SkillListing not found with id" + id)));
    }

    public SkillsListing createListing(SkillsListing listing) {
        return skillsListingRepo.save(listing);
    }

    public SkillsListing updateListing(int id, SkillsListing listingDetails) {
        return skillsListingRepo.findById(id).map(listing -> {
            listing.setTitle(listingDetails.getTitle());
            listing.setDescription(listingDetails.getDescription());
            listing.setPrice(listingDetails.getPrice());
            listing.setTime(listingDetails.getTime());
            listing.setAvgRating(listingDetails.getAvgRating());
            listing.setSkills(listingDetails.getSkills());
            listing.setSeller(listingDetails.getSeller());
            return skillsListingRepo.save(listing);
        }).orElseThrow(() -> new RuntimeException("Listing not found"));
    }

    public void deleteListing(int id) {
        skillsListingRepo.deleteById(id);
    }
}
