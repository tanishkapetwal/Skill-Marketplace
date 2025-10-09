package com.example.demo.seller.management.service;

import com.example.demo.order.management.dto.SellerRequestedOrdersDTO;
import com.example.demo.order.management.model.Orders;
import com.example.demo.order.management.repository.OrdersRepo;
import com.example.demo.seller.management.mapper.SellerMapper;
import com.example.demo.seller.management.model.Seller;
import com.example.demo.seller.management.repository.SellerRepo;
import com.example.demo.skill.management.dto.SkillsListingDTO;
import com.example.demo.skill.management.dto.SkillsResponseDTO;
import com.example.demo.skill.management.model.SkillsListing;
import com.example.demo.skill.management.repository.SkillsListingRepo;
import com.example.demo.skill.management.repository.SkillsRepo;
import com.example.demo.system.configuration.dto.SellerResponseDto;
import com.example.demo.system.configuration.email.management.dto.Notification;
import com.example.demo.system.configuration.email.management.model.EmailDetails;
import com.example.demo.order.management.constant.Status;
import com.example.demo.system.configuration.email.management.service.impl.EmailServiceImpl;
import com.example.demo.system.configuration.model.User;
import com.example.demo.system.configuration.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SellerService {

    private final OrdersRepo orderRepo;
    private final UserRepo userRepo;

    private final ModelMapper modelmapper;

    private final SkillsRepo skillsRepo;

    private final SellerRepo sellerRepo;

    private final SkillsListingRepo skillsListingRepo;

    private final SimpMessagingTemplate messagingTemplate;

    private final EmailServiceImpl emailService;
    private final SellerMapper sellerMapper;

    public SellerResponseDto getSellerById(Integer id) {
        return sellerMapper.sellerToSellerResponseDto(userRepo.findBySellerId(id).orElseThrow());
    }
    public void addSkillsListing (SkillsListing skillsListing){
            skillsListingRepo.save(skillsListing);
    }

    public List<SkillsResponseDTO> getSkills() {
        return sellerMapper.skillsToSkillsResponseDto(skillsRepo.findAll());
    }

    public User addSeller(User user) {
       Seller seller = new Seller();
       seller.setUser(user);
       sellerRepo.save(seller);
        user.setSeller(seller);
        return userRepo.save(user);

    }

    public void deleteSeller(Integer id) {
        sellerRepo.deleteById(id);
    }

    public List<SellerRequestedOrdersDTO> allOrderRequest(int seller_id) {
        return sellerMapper.ordersToSellerRequestedOrdersDTO(orderRepo.findBySkillslisting_SellerId(seller_id));
    }

    public String changeStatus(int  seller_id, int order_id, Status status) {
        Orders order = orderRepo.findById(order_id).orElseThrow();
        Integer sellerIdfromOrder = order.getSkillslisting().getSeller().getId();
        if(seller_id != sellerIdfromOrder)
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
                +"\nAppointment Date: "+ order.getAppointmentStart()+"\nEmail: "+ order.getSkillslisting().getSeller().getUser().getEmail()
                +"\nPhone Number "+ order.getSkillslisting().getSeller().getUser().getPhone()+
                "\n\n Best Regards\n" +
                "Team TechMate");
        return emailDetails;
    }

    public List<SellerResponseDto> getSellers() {
        return sellerMapper.sellerListToSellerResponseDto(sellerRepo.findAll());

    }

    public List<SkillsListingDTO> getListing(int sellerId){
        return sellerMapper.sellerSkillsToSkillsListingDto(skillsListingRepo.findBySellerId(sellerId));

    }
    public void deleteListing(int id) {
        skillsListingRepo.deleteById(id);
    }
}
