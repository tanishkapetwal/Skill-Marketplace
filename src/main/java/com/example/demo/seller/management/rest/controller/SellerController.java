package com.example.demo.seller.management.rest.controller;

import com.example.demo.order.management.constant.Status;
import com.example.demo.order.management.dto.SellerRequestedOrdersDTO;
import com.example.demo.seller.management.mapper.SellerMapper;
import com.example.demo.skill.management.dto.CreateListingDTO;
import com.example.demo.skill.management.dto.SkillsListingDTO;
import com.example.demo.skill.management.dto.SkillsResponseDTO;
import com.example.demo.system.configuration.dto.SellerResponseDto;
import com.example.demo.system.configuration.repository.UserRepo;
import com.example.demo.system.configuration.security.JWTService;
import com.example.demo.system.configuration.service.impl.AuthenticationService;
import com.example.demo.seller.management.service.SellerService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

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
    private final SellerMapper sellerMapper;
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
    public ResponseEntity<Void> addSkillsListing(@Valid @RequestBody CreateListingDTO createListingDTO, @PathVariable int skillId, HttpServletRequest request) {
        userId = getUserId(request);
        sellerservice.addSkillsListing(sellerMapper.skillToCreateListingDto(skillId, createListingDTO, userId));
        return  ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/order-request")
    public ResponseEntity<List<SellerRequestedOrdersDTO>> allOrderRequest(HttpServletRequest request){
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
