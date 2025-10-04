package com.example.demo.service;

import com.example.demo.model.EmailDetails;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class ResetPasswordService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private EmailService emailService;



    private static final String CHARACTER = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvxyz0123456789";

    private String generateRandomPassword(int length) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(CHARACTER.charAt(random.nextInt(CHARACTER.length())));
        }
        return sb.toString();
    }


    @Transactional
    public void resetPasswordForCurrentUser(String email) {
        User user = userRepo.findByEmail(email).orElseThrow();
        if (user == null) {
            throw new UsernameNotFoundException("User not available");
        }

        String newPassword = generateRandomPassword(10);
        user.setPassword(passwordEncoder.encode(newPassword));
        try {
            userRepo.save(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            EmailDetails emailDetails = getResetEmailDetails(newPassword,user);
            emailService.sendSimpleMail(emailDetails);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Transactional
    public void setNewPassword(String email,String newPass) {
        User user = userRepo.findByEmail(email).orElseThrow();
        if (user == null) {
            throw new UsernameNotFoundException("User not available");
        }
        user.setPassword(passwordEncoder.encode(newPass));
        try {
            userRepo.save(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        try {
            EmailDetails emailDetails = getResetEmailDetails(newPass,user);
            emailService.sendSimpleMail(emailDetails);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private static EmailDetails getResetEmailDetails(String newPassword,User user) {
        EmailDetails emailDetails = new EmailDetails();
        emailDetails.setRecipient(user.getEmail());
        emailDetails.setSubject("Password Changed Successfully!!!");
        emailDetails.setMsgBody("Dear "+ user.getName()+",\n We are happy to inform you that " +
                "your password has been changed Successfully.\n"+
                "New Password="+newPassword+

                "\n\n Best Regards\n" +
                "Team TechMate");
        return emailDetails;
    }
}
