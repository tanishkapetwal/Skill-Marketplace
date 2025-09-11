package com.example.demo.service;

import com.example.demo.dto.LoginUserDto;
import com.example.demo.dto.RegisterSellerDto;
import com.example.demo.dto.RegisterCustomerDto;
import com.example.demo.model.Customer;
import com.example.demo.model.Seller;
import com.example.demo.model.User;
import com.example.demo.model.type.Role;
import com.example.demo.repository.CustomerRepo;
import com.example.demo.repository.SellerRepo;
import com.example.demo.repository.UserRepo;
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

    private final UserRepo userRepo;

    public AuthenticationService(
            CustomerRepo customerRepo,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            SellerRepo sellerRepo,
            UserRepo userRepo
    ) {
        this.authenticationManager = authenticationManager;
        this.customerRepo= customerRepo ;
        this.passwordEncoder = passwordEncoder;
        this.sellerRepo = sellerRepo;
        this.userRepo = userRepo;
    }

    public User signup(RegisterCustomerDto input) {
        User user = new User();
        Customer cust = new Customer();

        user.setName(input.getName());
        user.setPhone(input.getPhone());
        user.setEmail(input.getEmail());
        user.setRole(Role.CUSTOMER);
        user.setPassword(passwordEncoder.encode(input.getPassword()));

        User savedUser = userRepo.save(user);
        cust.setUser(savedUser);
        customerRepo.save(cust);
        return savedUser;
//        input.setPassword(passwordEncoder.encode(input.getPassword()));
//        Customer customer = modelMapper.map(input, Customer.class);
//        return customerRepo.save(customer);
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
    public Authentication authenticate(LoginUserDto input) {
        Authentication authentication= authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    public int fetchUserId(UserDetails userDetails){
        User user = userRepo.findByEmail(userDetails.getUsername()).orElseThrow(RuntimeException::new);
        return user.getId();
    }

    public User signupAdmin(RegisterCustomerDto input) {

        User user = new User();
        user.setName(input.getName());
        user.setPhone(input.getPhone());
        user.setEmail(input.getEmail());
        user.setRole(Role.ADMIN);
        user.setPassword(passwordEncoder.encode(input.getPassword()));

        User savedUser = userRepo.save(user);
        return savedUser;
    }
}

