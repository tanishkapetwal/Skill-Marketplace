package com.example.demo.controller;

import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.LoginUserDto;
import com.example.demo.dto.RegisterSellerDto;
import com.example.demo.dto.RegisterUserDto;
import com.example.demo.model.Customer;
import com.example.demo.security.JWTService;
import com.example.demo.service.AuthenticationService;
import com.example.demo.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import com.example.demo.model.Seller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("seller")
public class SellerController {

    @Autowired
    private AuthenticationService authenticationService;
    private JWTService jwtService;
    @Autowired
    private SellerService service;

    @GetMapping(value={"/", "{id}"})
    public List<Seller> getSeller(@PathVariable (required = false) Integer id){
        if(id!= null)return service.getSellerById(id);
        else return service.getSeller();
    }

    @PostMapping("/add")
    public void addSeller(@RequestBody Seller seller){
        service.addSeller(seller);
    }

    @DeleteMapping("/delete")
    public void deleteSeller(@PathVariable int id){
        service.deleteSeller(id);
    }

    @PostMapping("/signup")
    public ResponseEntity<Seller> addSeller(@RequestBody RegisterSellerDto registerSellerDto){
        Seller registeredSeller = authenticationService.signupSeller(registerSellerDto);

        return ResponseEntity.ok(registeredSeller);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        Authentication authentication = authenticationService.authenticate(loginUserDto);

        UserDetails authenticatedUser = (UserDetails) authentication.getPrincipal();
        String jwtToken = jwtService.generateToken(authenticatedUser);
        LoginResponse loginResponse = LoginResponse.builder().token(jwtToken).expiresIn(jwtService.getExpirationTime()).build();

        return ResponseEntity.ok(loginResponse);
    }

}
