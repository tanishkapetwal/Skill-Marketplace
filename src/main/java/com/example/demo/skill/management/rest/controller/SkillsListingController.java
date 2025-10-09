package com.example.demo.skill.management.rest.controller;

import com.example.demo.skill.management.model.SkillsListing;
import com.example.demo.skill.management.service.SkillsListingService;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/skills-listings")
public class SkillsListingController {

    private final SkillsListingService skillsListingService;

    public SkillsListingController(SkillsListingService skillsListingService) {
        this.skillsListingService = skillsListingService;
    }

    @GetMapping
    public List<SkillsListing> getAllListings() {
        return skillsListingService.getAllListings();
    }

    @GetMapping("/{id}")
    public List<SkillsListing> getListingById(@PathVariable int id) {
        return skillsListingService.getListingById(id);
    }

    @PostMapping
    public SkillsListing createListing(@RequestBody SkillsListing listing) {
        return skillsListingService.createListing(listing);
    }

    @PutMapping("/{id}")
    public SkillsListing updateListing(@PathVariable int id, @RequestBody SkillsListing listing) {
        return skillsListingService.updateListing(id, listing);
    }

    @DeleteMapping("/{id}")
    public void deleteListing(@PathVariable int id) {
        skillsListingService.deleteListing(id);
    }
}
