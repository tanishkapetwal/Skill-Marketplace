package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.model.*;

import com.example.demo.security.JWTService;
import com.example.demo.service.AuthenticationService;
import com.example.demo.service.SellerService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
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
    private SellerService sellerservice;
    int userId;

    public int getUserId(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.split(" ")[1];
        userId = jwtService.extractClaim(token, claims -> claims.get("id", Integer.class));
        return userId;
    }

    @GetMapping(value = {"/"})
    public User getSeller(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.split(" ")[1];
        int id = jwtService.extractClaim(token, claims -> claims.get("id", Integer.class));
        return sellerservice.getSellerById(id);

    }

//    @DeleteMapping("/delete")
//    public void deleteSeller(@PathVariable int id){
//        service.deleteSeller(id);
//    }

    @PostMapping("/signup")
    public ResponseEntity<User> addSeller(@RequestBody RegisterSellerDto registerSellerDto) {
        User registeredSeller = authenticationService.signupSeller(registerSellerDto);

        return ResponseEntity.ok(registeredSeller);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        Authentication authentication = authenticationService.authenticate(loginUserDto);

        UserDetails authenticatedUser = (UserDetails) authentication.getPrincipal();

        int  id = authenticationService.fetchUserId(authenticatedUser);

        String jwtToken = jwtService.generateToken(authenticatedUser, id);
        LoginResponse loginResponse = LoginResponse.builder().token(jwtToken).expiresIn(jwtService.getExpirationTime()).build();

        return ResponseEntity.ok(loginResponse);
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

}
