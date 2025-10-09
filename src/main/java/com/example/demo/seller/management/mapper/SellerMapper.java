package com.example.demo.seller.management.mapper;

import com.example.demo.order.management.dto.SellerRequestedOrdersDTO;
import com.example.demo.order.management.model.Orders;
import com.example.demo.seller.management.model.Seller;
import com.example.demo.seller.management.repository.SellerRepo;
import com.example.demo.skill.management.dto.CreateListingDTO;
import com.example.demo.skill.management.dto.SkillsListingDTO;
import com.example.demo.skill.management.dto.SkillsResponseDTO;
import com.example.demo.skill.management.model.Skills;
import com.example.demo.skill.management.model.SkillsListing;
import com.example.demo.skill.management.repository.SkillsRepo;
import com.example.demo.system.configuration.dto.SellerResponseDto;
import com.example.demo.system.configuration.exception.ResourceNotFoundException;
import com.example.demo.system.configuration.model.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SellerMapper {
    private final ModelMapper modelMapper;
    private final SkillsRepo skillsRepo;
    private final SellerRepo sellerRepo;
    public SellerResponseDto sellerToSellerResponseDto(User user){
        return modelMapper.map(user, SellerResponseDto.class);
    }

    public SkillsListing skillToCreateListingDto(int skillId, CreateListingDTO createListingDTO, int userId) {

        SkillsListing skillsListing = new SkillsListing();
        skillsListing.setSkills(skillsRepo.findById(skillId).orElseThrow(() -> new ResourceNotFoundException("Skill not found with id" + skillId)));

        skillsListing.setSeller(sellerRepo.findById(userId).orElseThrow(()->new ResourceNotFoundException("Seller not found with tihs id")));

        skillsListing.setTitle(createListingDTO.getTitle());

        skillsListing.setDescription(createListingDTO.getDescription());
        skillsListing.setPrice(createListingDTO.getPrice());
        skillsListing.setTime(createListingDTO.getTime());
       return skillsListing;

    }
    public List<SkillsResponseDTO> skillsToSkillsResponseDto(List<Skills> skills){

        return skills.stream().
                map(skill -> modelMapper.map(skill, SkillsResponseDTO.class)).toList();
    }
    public List<SellerRequestedOrdersDTO> ordersToSellerRequestedOrdersDTO(List<Orders> orders){
        return orders.stream().map(order->modelMapper.map(order, SellerRequestedOrdersDTO.class)).
                                                toList();
    }

    public List<SellerResponseDto> sellerListToSellerResponseDto(List<Seller> sellers){
        return sellers.stream().map(seller -> modelMapper.map(seller, SellerResponseDto.class)).toList();
    }

    public List<SkillsListingDTO> sellerSkillsToSkillsListingDto(List<SkillsListing> skillsListingList) {
        return skillsListingList.stream()
                .map(s->modelMapper.map(s,SkillsListingDTO.class)).toList();
    }

}
