package com.example.demo.system.configuration.service.impl;

import com.example.demo.system.configuration.dto.LoginUserDto;
import com.example.demo.system.configuration.dto.RegisterSellerDto;

import com.example.demo.system.configuration.dto.RegisterCustomerDto;

import com.example.demo.system.configuration.exception.UserNotFoundException;
import com.example.demo.customer.managment.model.Customer;
import com.example.demo.seller.management.model.Seller;
import com.example.demo.system.configuration.model.User;
import com.example.demo.system.configuration.constant.Role;
import com.example.demo.customer.managment.repository.CustomerRepo;
import com.example.demo.seller.management.repository.SellerRepo;
import com.example.demo.system.configuration.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private ModelMapper modelMapper;

    private final CustomerRepo customerRepo;

    private final SellerRepo sellerRepo;

    private final PasswordEncoder passwordEncoder;

//    private final AuthenticationManager authenticationManager;

    private final UserRepo userRepo;
    public User signup(RegisterCustomerDto input) {
        User user = new User();
        Customer cust = new Customer();

        user.setName(input.getName());
        user.setPhone(input.getPhone());
        user.setEmail(input.getEmail());
        user.setRole(Role.CUSTOMER);
        user.setPassword(passwordEncoder.encode(input.getPassword()));

        try{

          User savedUser = userRepo.save(user);
          cust.setUser(savedUser);
          customerRepo.save(cust);
          return savedUser;

      } catch (Exception e) {
          throw new RuntimeException(e);
      }
    }

    public User signupSeller(RegisterSellerDto input) {

        User user = new User();
        Seller seller = new Seller();

        user.setName(input.getName());
        user.setPhone(input.getPhone());
        user.setEmail(input.getEmail());
        user.setRole(Role.SELLER);
        user.setPassword(passwordEncoder.encode(input.getPassword()));

        User savedUser = userRepo.save(user);
        seller.setBio(input.getBio());
        seller.setUser(savedUser);
        sellerRepo.save(seller);
        return savedUser;
    }

    public int fetchAdminId(UserDetails userDetails){
        User user = userRepo.findByEmail(userDetails.getUsername()).orElseThrow(()->new UserNotFoundException("Admin Not Found"));
        return user.getId();
    }
    public int fetchCustomerId(UserDetails userDetails){
        User user = userRepo.findByEmail(userDetails.getUsername()).orElseThrow(()->new UserNotFoundException("User Not Found"));
        System.out.println(userDetails.getUsername());
        Customer customer = customerRepo.findByUserId(user.getId()).orElseThrow(()->new UserNotFoundException("Customer Not Found"));
        return customer.getId();
    }
    public int fetchSellerId(UserDetails userDetails){
        User user = userRepo.findByEmail(userDetails.getUsername()).orElseThrow(()->new UserNotFoundException("User Not Found"));
        Seller seller = sellerRepo.findByUserId(user.getId()).orElseThrow(()->new UserNotFoundException("Seller Not Found"));
        return seller.getId();

    }
    public User signupCustomerByAdmin(RegisterCustomerDto input) {

        User user = new User();
        user.setName(input.getName());
        user.setPhone(input.getPhone());
        user.setEmail(input.getEmail());
        user.setRole(Role.CUSTOMER);
        user.setPassword(passwordEncoder.encode(input.getPassword()));

        User savedUser = userRepo.save(user);
        return savedUser;
    }
    public User signupSellerByAdmin(RegisterSellerDto input) {

        User user = new User();
        user.setName(input.getName());
        user.setPhone(input.getPhone());
        user.setEmail(input.getEmail());
        user.setRole(Role.SELLER);
        user.setPassword(passwordEncoder.encode(input.getPassword()));

        User savedUser = userRepo.save(user);
        return savedUser;
    }

    public int findUserId(UserDetails authenticatedUser) {
        User user= userRepo.findByEmail(authenticatedUser.getUsername()).orElseThrow(RuntimeException::new);
        if(user.getRole().equals(Role.SELLER))
            return fetchSellerId(authenticatedUser);
        else if(user.getRole().equals(Role.ADMIN))
            return fetchAdminId(authenticatedUser);
        else if(user.getRole().equals(Role.CUSTOMER))
            return fetchCustomerId(authenticatedUser);
        return 0;

    }

    public String getUser(String username) {

        return userRepo.findByEmail(username).orElseThrow(RuntimeException::new).getRole().name();
    }
}

