package com.example.demo.service;

import com.example.demo.dto.CreateListingDTO;
import com.example.demo.dto.SellerOrdersDTO;
import com.example.demo.dto.SkillsResponseDTO;
import com.example.demo.model.Orders;
import com.example.demo.model.SkillsListing;

import com.example.demo.model.User;
import com.example.demo.model.type.Status;
import com.example.demo.repository.*;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SellerService {

    @Autowired
    private OrdersRepo orderRepo;
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

    public List<SellerOrdersDTO> allOrderRequest(int userId) {
        int seller_id = sellerRepo.findByUserId(userRepo.findById(userId).orElseThrow().getId()).orElseThrow().getId();

        List<SellerOrdersDTO> sellerOrdersDTOS =  orderRepo.findBySkillslisting_SellerId(seller_id).
                                                stream().map(orders->modelmapper.map(orders, SellerOrdersDTO.class)).
                                                toList();

       return sellerOrdersDTOS;
    }

    public void changeStatus(int userId, int order_id, Status status) {
        Integer seller_id = sellerRepo.findByUserId(userRepo.findById(userId).orElseThrow().getId()).orElseThrow().getId();
        Orders order = orderRepo.findById(order_id).orElseThrow();
        Integer sellerIdfromOrder = order.getSkillslisting().getSeller().getId();
        if(!seller_id.equals(sellerIdfromOrder))
            throw new RuntimeException("Can't access this page!");

        order.setStatus(status);
        orderRepo.save(order);

    }
}
