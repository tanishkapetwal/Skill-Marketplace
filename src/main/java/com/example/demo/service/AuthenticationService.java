package com.example.demo.service;

import com.example.demo.dto.LoginUserDto;
import com.example.demo.dto.RegisterUserDto;
import com.example.demo.model.Customer;
import com.example.demo.repository.CustomerRepo;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {
    @Autowired
    private ModelMapper modelMapper;

    private final CustomerRepo customerRepo;

    private final PasswordEncoder passwordEncoder;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
            CustomerRepo customerRepo,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.customerRepo= customerRepo ;
        this.passwordEncoder = passwordEncoder;
    }

    public Customer signup(RegisterUserDto input) {
        Customer customer = new Customer();
        input.setPassword(passwordEncoder.encode(input.getPassword()));
        customer = modelMapper.map(input, Customer.class);
        return customerRepo.save(customer);
    }

    public Customer authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getEmail(),
                        input.getPassword()
                )
        );

        return customerRepo.findByEmail(input.getEmail())
                .orElseThrow();
    }
}

