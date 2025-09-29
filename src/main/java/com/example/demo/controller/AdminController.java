package com.example.demo.controller;


import com.example.demo.dto.*;
import com.example.demo.model.Customer;
import com.example.demo.model.Seller;
import com.example.demo.model.Skills;
import com.example.demo.model.User;
import com.example.demo.security.JWTService;
import com.example.demo.service.*;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


// @CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
@RestController

@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private final AdminService adminService;
    @Autowired
    private final AuthenticationService authenticationService;
    @Autowired
    private final SkillsService skillsService;
    @Autowired
    private final CustomerService customerService;
    @Autowired
    private final SellerService sellerService;
    @Autowired
    private final JWTService jwtService;
    @Autowired
    private final UserDetailsService userDetailsService;

    @PostMapping("/add-skills")
    public ResponseEntity<Skills> addSkills(@RequestBody Skills skill){
        return new ResponseEntity<>(skillsService.addSkills(skill), HttpStatus.OK);
    }
    @PostMapping("/add-customer")
    public ResponseEntity<User> addCustomers(@RequestBody RegisterCustomerDto customer){
        customerService.addCustomers(customer);
        User registeredUser = authenticationService.signupCustomerByAdmin(customer);
        return ResponseEntity.ok(registeredUser);
    }
    @GetMapping("/all-customers")
    public List<CustomerResponseDto> getCustomers(){
        return customerService.getCustomers();

    }
    @GetMapping("/all-admins")
    public List<CustomerResponseDto> getAdmins(){
        return adminService.getAdmins();

    }
    @PostMapping("/add-seller")
    public ResponseEntity<User> addSeller(@RequestBody RegisterSellerDto seller){
        sellerService.addSeller(seller);
        User registeredUser = authenticationService.signupSellerByAdmin(seller);
        return ResponseEntity.ok(registeredUser);
    }
    @GetMapping("/all-sellers")
    public List<SellerResponseDto> getSellers(){
        return sellerService.getSellers();

    }
    @DeleteMapping("/remove/skill/{id}")
    public ResponseEntity<Void> deleteSkills(@PathVariable int id){
        skillsService.deleteSkill(id);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/remove/customer/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable int id){
        adminService.deleteCustomer(id);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/remove/seller/{id}")
    public ResponseEntity<Void> deleteSeller(@PathVariable int id){
        adminService.deleteSeller(id);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/add-admin")
    public ResponseEntity<User> addAdmin(@RequestBody RegisterCustomerDto registerUserDto){
        User registeredUser = adminService.signupAdmin(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }

    @PutMapping("/{id}")
    public Skills updateSkill(@PathVariable int id, @RequestBody Skills skill) {
        return adminService.updateSkill(id, skill);
    }
    @GetMapping("/skills")
    public ResponseEntity<List<SkillsResponseDTO>>  getAllSkills(){
        return new ResponseEntity<>(sellerService.getSkills(), HttpStatus.OK);
    }
}
