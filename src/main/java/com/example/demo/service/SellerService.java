package com.example.demo.service;

import com.example.demo.dto.*;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.*;

import com.example.demo.model.type.Status;
import com.example.demo.repository.*;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import org.springframework.messaging.simp.SimpMessagingTemplate.*;

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
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private EmailService emailService;

    public User getSellerById(Integer id) {
        return userRepo.findById(id).orElseThrow();

    }
        public SkillsListing addSkillsListing ( int skillId, CreateListingDTO createListingDTO,int sellerId){
            SkillsListing skillsListing = new SkillsListing();


//        skillsListing = modelmapper.map(createListingDTO, SkillsListing.class);
//            skillsListing = modelmapper.map(createListingDTO, SkillsListing.class);

            skillsListing.setSkills(skillsRepo.findById(skillId).orElseThrow(() -> new ResourceNotFoundException("User not found with id" + skillId)));

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
    public Seller addSeller(RegisterSellerDto registerSeller) {

        Seller seller = modelmapper.map(registerSeller, Seller.class);
        return sellerRepo.save(seller);

    }
    public void deleteSeller(Integer id) {
        sellerRepo.deleteById(id);
    }
    public List<SellerOrdersDTO> allOrderRequest(int userId) {
        int seller_id = sellerRepo.findByUserId(userRepo.findById(userId).orElseThrow().getId()).orElseThrow().getId();

        List<SellerOrdersDTO> sellerOrdersDTOS =  orderRepo.findBySkillslisting_SellerId(seller_id).
                                                stream().map(orders->modelmapper.map(orders, SellerOrdersDTO.class)).
                                                toList();

       return sellerOrdersDTOS;
    }

    public String changeStatus(int userId, int order_id, Status status) {
        Integer seller_id = sellerRepo.findByUserId(userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id" + userId)).getId()).orElseThrow(() -> new ResourceNotFoundException("Seller not found with this id")).getId();
        Orders order = orderRepo.findById(order_id).orElseThrow();
        Integer sellerIdfromOrder = order.getSkillslisting().getSeller().getId();
        if(!seller_id.equals(sellerIdfromOrder))
            throw new RuntimeException("Can't access this page!");

        order.setStatus(status);
        orderRepo.save(order);

        Notification notification = new Notification( order_id,"Order status changed to"+status.toString().toUpperCase());
        messagingTemplate.convertAndSend("/topic/orders/"+order.getCustomer().getUser().getId(), notification);

        if(status.toString().equals("ACCEPTED")){
            EmailDetails emailDetails = getEmailDetails(order_id, order);
            emailService.sendSimpleMail(emailDetails);
        }
        return "Order " +order_id+"updated to"+status.toString();
    }

    private static EmailDetails getEmailDetails(int order_id, Orders order) {
        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setRecipient(order.getCustomer().getUser().getEmail());
        emailDetails.setSubject("Order Request for "+ order_id +" has been accepted");
        emailDetails.setMsgBody("Dear "+ order.getCustomer().getUser().getName()+",\n We are happy to inform you that " +
                "your order has been accepted by the teacher.\n"+
                "For connecting with teacher here are the details:\n"+"Name: "
                + order.getSkillslisting().getSeller().getUser().getName()+"\nSkill: "+ order.getSkillslisting().getSkills().getName()
                +"\nAppointment Date: "+ order.getAppointmentStart()+"\nEmail: "+ order.getCustomer().getUser().getEmail()
                +"\nPhone Number "+ order.getCustomer().getUser().getPhone()+
                "\n\n Best Regards\n" +
                "Team TechMate");
        return emailDetails;
    }


    public List<SellerResponseDto> getSellers() {
        return (sellerRepo.findAll().stream().map
                (seller -> modelmapper.map(seller, SellerResponseDto.class)).toList());
    }




    public List<SkillsListingDTO> getListing(int userId){
        Integer seller_id = sellerRepo.findByUserId(userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found with id" + userId)).getId()).orElseThrow(() -> new ResourceNotFoundException("Seller not found with this id")).getId();
        List<SkillsListingDTO> obj= skillsListingRepo.findBySellerId(seller_id).
                                    stream().map(s->modelmapper.map(s,SkillsListingDTO.class)).toList();
        return obj;
    }

    public void deleteListing(int id) {
        skillsListingRepo.deleteById(id);
    }
}
