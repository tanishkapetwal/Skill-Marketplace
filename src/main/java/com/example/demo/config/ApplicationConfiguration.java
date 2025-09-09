package com.example.demo.config;

import com.example.demo.repository.CustomerRepo;
import com.example.demo.repository.SellerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class ApplicationConfiguration {
    private final CustomerRepo customerRepo;
    private final SellerRepo sellerRepo;

    @Autowired
    public ApplicationConfiguration(CustomerRepo customerRepo, SellerRepo sellerRepo) {
        this.customerRepo = customerRepo;
        this.sellerRepo = sellerRepo;
    }


    @Bean
    UserDetailsService userDetailsService() {
//        return username -> customerRepo.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User Not found"));

        return username->customerRepo.findByEmail(username)
                .map(c -> User.withUsername(c.getUsername())
                        .password(c.getPassword())
                        .roles("CUSTOMER")
                        .build())
                .orElseGet(() -> sellerRepo.findByEmail(username)
                        .map(s -> User.withUsername(s.getUsername())
                                .password(s.getPassword())
                                .roles("SELLER")
                                .build())
                        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username)));
    }


    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception{
        return config.getAuthenticationManager();
    }

    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

}
