package com.example.demo.system.configuration.email.management.service.impl;

import com.example.demo.system.configuration.email.management.model.EmailDetails;
import com.example.demo.skill.management.model.SkillsListing;
import com.example.demo.system.configuration.model.User;
import com.example.demo.skill.management.repository.SkillsListingRepo;
import com.example.demo.system.configuration.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendEmail {

    private final SkillsListingRepo skillsListingRepo;
    private final UserRepo userRepo;
    private final EmailServiceImpl emailService;
    public void sendEmail(int listingId) {
        SkillsListing skillsListing=skillsListingRepo.findById(listingId).orElseThrow();
        int sellerId = skillsListing.getSeller().getId();

        User user = userRepo.findBySellerId(sellerId).orElseThrow();
        String email = user.getEmail();

        EmailDetails emailDetails = getSellerEmailDetails(email);
        emailService.sendSimpleMail(emailDetails);
        System.out.println(sellerId);
    }


    private EmailDetails getSellerEmailDetails(String email) {

        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setRecipient(email);
        emailDetails.setSubject("Order Request for  has been Added");
        emailDetails.setMsgBody("Dear "+ ",\n Seller " +
                "You have a new order.\n Please check\n"+
                "\n\n Best Regards\n" +
                "Team TechMate");
        return emailDetails;
    }
}
