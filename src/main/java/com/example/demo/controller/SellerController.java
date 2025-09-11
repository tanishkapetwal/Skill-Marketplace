package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.model.Customer;
import com.example.demo.model.SkillsListing;
import com.example.demo.model.User;
import com.example.demo.security.JWTService;
import com.example.demo.service.AuthenticationService;
import com.example.demo.service.SellerService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import com.example.demo.model.Seller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seller")
public class SellerController {

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private SellerService service;
    int userId;

    public int getUserId(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.split(" ")[1];
        userId = jwtService.extractClaim(token, claims -> claims.get("id", Integer.class));
        return userId;
    }
    @GetMapping(value={"/"})
    public User getSeller(HttpServletRequest request){
        String authHeader= request.getHeader("Authorization");
        String token = authHeader.split(" ")[1];
        int id = jwtService.extractClaim(token,claims -> claims.get("id", Integer.class));
        return service.getSellerById(id);

    }

    @DeleteMapping("/delete")
    public void deleteSeller(@PathVariable int id){
        service.deleteSeller(id);
    }

    @PostMapping("/signup")
    public ResponseEntity<User> addSeller(@RequestBody RegisterSellerDto registerSellerDto){
        User registeredSeller = authenticationService.signupSeller(registerSellerDto);

        return ResponseEntity.ok(registeredSeller);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        Authentication authentication = authenticationService.authenticate(loginUserDto);

        UserDetails authenticatedUser = (UserDetails) authentication.getPrincipal();
        userId = authenticationService.fetchUserId(authenticatedUser);
        System.out.println(userId);
        String jwtToken = jwtService.generateToken(authenticatedUser,userId);
        LoginResponse loginResponse = LoginResponse.builder().token(jwtToken).expiresIn(jwtService.getExpirationTime()).build();

        return ResponseEntity.ok(loginResponse);
    }
    
    @PostMapping("/add-to-listing/{skillId}")
    public ResponseEntity<SkillsListing> addSkillsListing(@RequestBody CreateListingDTO createListingDTO, @PathVariable int skillId){


        service.addSkillsListing(skillId, createListingDTO, userId);
        return ResponseEntity.ok().build();}


}
