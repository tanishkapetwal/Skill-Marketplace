package com.example.demo.service;

import com.example.demo.dto.CreateListingDTO;
import com.example.demo.dto.SkillsResponseDTO;
import com.example.demo.model.Seller;
import com.example.demo.model.Skills;
import com.example.demo.model.SkillsListing;

import com.example.demo.model.User;
import com.example.demo.repository.SellerRepo;
import com.example.demo.repository.SkillsListingRepo;
import com.example.demo.repository.SkillsRepo;
import com.example.demo.repository.UserRepo;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class SellerService {

    @Autowired
    private UserRepo userRepo;
    @Autowired
    private ModelMapper modelmapper;
    @Autowired
    private SkillsRepo skillsRepo;
    @Autowired
    private SellerRepo sellerRepo;
    @Autowired
    private SkillsListingRepo skillsListingRepo;

    public User getSellerById(Integer id) {
        return userRepo.findById(id).orElseThrow();

    }
        public SkillsListing addSkillsListing ( int skillId, CreateListingDTO createListingDTO,int sellerId){
            SkillsListing skillsListing = new SkillsListing();


//        skillsListing = modelmapper.map(createListingDTO, SkillsListing.class);
//            skillsListing = modelmapper.map(createListingDTO, SkillsListing.class);

            skillsListing.setSkills(skillsRepo.findById(skillId).orElseThrow(RuntimeException::new));

            skillsListing.setSeller(sellerRepo.findByUserId(userRepo.findById(sellerId).orElseThrow().getId()).orElseThrow());

            skillsListing.setTitle(createListingDTO.getTitle());

            skillsListing.setDescription(createListingDTO.getDescription());
            skillsListing.setPrice(createListingDTO.getPrice());
            skillsListing.setTime(createListingDTO.getTime());


          return  skillsListingRepo.save(skillsListing);
        }

    public List<SkillsResponseDTO> getSkills() {

        List<SkillsResponseDTO> skillsResponseDTOS = skillsRepo.findAll().stream().
                map(skills -> modelmapper.map(skills, SkillsResponseDTO.class)).toList();
        return skillsResponseDTOS;
//        return skillsRepo.findAll();

    }
}
