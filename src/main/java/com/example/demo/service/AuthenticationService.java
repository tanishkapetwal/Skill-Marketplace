package com.example.demo.service;

import com.example.demo.dto.LoginUserDto;
import com.example.demo.dto.RegisterSellerDto;
import com.example.demo.dto.RegisterUserDto;
import com.example.demo.model.Customer;
import com.example.demo.model.Seller;
import com.example.demo.repository.CustomerRepo;
import com.example.demo.repository.SellerRepo;
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
public class AuthenticationService {
    @Autowired
    private ModelMapper modelMapper;

    private final CustomerRepo customerRepo;

    private final SellerRepo sellerRepo;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            CustomerRepo customerRepo,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            SellerRepo sellerRepo
    ) {
        this.authenticationManager = authenticationManager;
        this.customerRepo= customerRepo ;
        this.passwordEncoder = passwordEncoder;
        this.sellerRepo = sellerRepo;
    }

    public Customer signup(RegisterUserDto input) {

        input.setPassword(passwordEncoder.encode(input.getPassword()));
        Customer customer = modelMapper.map(input, Customer.class);
        return customerRepo.save(customer);
    }

    public Authentication authenticate(LoginUserDto input) {
        Authentication authentication= authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
//        return customerRepo.findByEmail(input.getEmail()).orElseThrow();
    }

    public int fetchCustomerId(UserDetails user){
        Customer customer = customerRepo.findByEmail(user.getUsername()).orElseThrow(RuntimeException::new);
        return customer.getId();

    }
    public int fetchSellerId(UserDetails user){
        Seller seller = sellerRepo.findByEmail(user.getUsername()).orElseThrow(RuntimeException::new);
        return seller.getId();
    }
    public Seller signupSeller(RegisterSellerDto input) {

        input.setPassword(passwordEncoder.encode(input.getPassword()));
        Seller seller = modelMapper.map(input, Seller.class);
        return sellerRepo.save(seller);
    }
}

