package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.model.*;

import com.example.demo.model.type.Role;
import com.example.demo.model.type.Status;
import com.example.demo.repository.UserRepo;
import com.example.demo.security.JWTService;
import com.example.demo.service.AuthenticationService;
import com.example.demo.service.SellerService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.management.Notification;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

// @CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/seller")
@RequiredArgsConstructor
public class SellerController {

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private JWTService jwtService;
    @Autowired
    private SellerService sellerservice;
    @Autowired
    private UserDetailsService userDetailsService;
    private final UserRepo userRepo;

    int userId;

    public int getUserId(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.split(" ")[1];
        userId = jwtService.extractClaim(token, claims -> claims.get("id", Integer.class));
        System.out.println(userId);
        return userId;
    }

    @GetMapping(value = {"/"})
    public SellerResponseDto getSeller(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.split(" ")[1];
        int id = jwtService.extractClaim(token, claims -> claims.get("id", Integer.class));
        return sellerservice.getSellerById(id);

    }

    @GetMapping("/skills")
    public ResponseEntity<List<SkillsResponseDTO>>  getSkills(){

        return new ResponseEntity<>(sellerservice.getSkills(), HttpStatus.OK);
    }

    @PostMapping("/add-to-listing/{skillId}")
    public ResponseEntity<Void> addSkillsListing(@RequestBody CreateListingDTO createListingDTO, @PathVariable int skillId, HttpServletRequest request) {
        userId = getUserId(request);
        sellerservice.addSkillsListing(skillId, createListingDTO, userId);
        return  ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/order-request")
    public ResponseEntity<List<SellerOrdersDTO>> allOrderRequest(HttpServletRequest request){
        userId = getUserId(request);
        return new ResponseEntity<>( sellerservice.allOrderRequest(userId), HttpStatus.OK);
    }
    @GetMapping("/skill-listings")
    public ResponseEntity<List<SkillsListingDTO>> getListing(HttpServletRequest request){
        userId = getUserId(request);
        return new ResponseEntity<>( sellerservice.getListing(userId), HttpStatus.OK);
    }

    @PutMapping("/{order_id}/change-status")
    public ResponseEntity<String> changeStatus(@PathVariable int order_id, @RequestParam Status status, HttpServletRequest request){
        userId = getUserId(request);
        String message = sellerservice.changeStatus(userId, order_id, status);

        return new ResponseEntity<>(message, HttpStatus.OK);
    }
    @DeleteMapping("/delete/{listingId}")
    public void deleteListing(@PathVariable int listingId) {
        sellerservice.deleteListing(listingId);
    }
}
