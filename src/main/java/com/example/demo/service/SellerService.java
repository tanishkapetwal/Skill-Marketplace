package com.example.demo.service;

import com.example.demo.dto.CreateListingDTO;
import com.example.demo.model.Seller;
import com.example.demo.model.SkillsListing;
import com.example.demo.repository.SellerRepo;
import com.example.demo.repository.SkillsListingRepo;
import com.example.demo.repository.SkillsRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SellerService {

    @Autowired
    private ModelMapper modelmapper;
    @Autowired
    private SkillsRepo skillsRepo;
    @Autowired
    private SellerRepo sellerRepo;
    @Autowired
    private SkillsListingRepo skillsListingRepo;

    public List<Seller> getSeller() {
        return sellerRepo.findAll();
    }

    public List<Seller> getSellerById(Integer id) {
        return sellerRepo.findAllById(Collections.singleton(id));
    }

    public void addSeller(Seller seller) {
        sellerRepo.save(seller);
    }

    public void deleteSeller(int id) {
        sellerRepo.deleteById(id);
    }
    public void addSkillsListing(int skillId, CreateListingDTO createListingDTO, int sellerId) {
        SkillsListing skillsListing=new SkillsListing();

        skillsListing = modelmapper.map(createListingDTO, SkillsListing.class);
        skillsListing.setSkills(skillsRepo.findById(skillId).orElseThrow(RuntimeException::new));
        skillsListing.setSeller(sellerRepo.findById(sellerId).orElseThrow(RuntimeException::new));
        skillsListing.setTitle(createListingDTO.getTitle());
        skillsListing.setDescription(createListingDTO.getDescription());

        skillsListingRepo.save(skillsListing);}

}
