package com.example.demo.config;

import com.example.demo.repository.CustomerRepo;
import com.example.demo.repository.SellerRepo;
import com.example.demo.repository.UserRepo;
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
    private final UserRepo userRepo;

    @Autowired
    public ApplicationConfiguration(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Bean
    UserDetailsService userDetailsService() {

        return username -> userRepo.findByEmail(username).orElseThrow(()->new UsernameNotFoundException("User not found"));

//        return username -> customerRepo.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User Not found"));

//        return username->userRepo.findByEmail(username)
//                .map(c -> User.withUsername(c.getUs)
//                        .password(c.getPassword())
//                        .roles("CUSTOMER")
//                        .build())
//                .orElseGet(() -> sellerRepo.findByEmail(username)
//                        .map(s -> User.withUsername(s.getUser())
//                                .password(s.getPassword())
//                                .roles("SELLER")
//                                .build())
//                .orElseGet(()->adminRepo.findByEmail(username)
//                                .map(a->User.withUsername(a.getUsername()))
//                                .password(a.getPassword())
//                                .roles("ADMIN")
//                                .build())
//                        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username)));
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
