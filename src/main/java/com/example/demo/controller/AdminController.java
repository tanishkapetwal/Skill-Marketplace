package com.example.demo.controller;


import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.LoginUserDto;
import com.example.demo.dto.RegisterCustomerDto;
import com.example.demo.dto.RegisterSellerDto;
import com.example.demo.model.Customer;
import com.example.demo.model.Seller;
import com.example.demo.model.Skills;
import com.example.demo.model.User;
import com.example.demo.security.JWTService;
import com.example.demo.service.AuthenticationService;
import com.example.demo.service.CustomerService;
import com.example.demo.service.SellerService;
import com.example.demo.service.SkillsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController

@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private SkillsService skillsService;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private SellerService sellerService;
    @Autowired
    private JWTService jwtService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        Authentication authentication = authenticationService.authenticate(loginUserDto);

        UserDetails authenticatedUser = (UserDetails) authentication.getPrincipal();

        int userId = authenticationService.fetchUserId(authenticatedUser);
        String jwtToken = jwtService.generateToken(authenticatedUser,userId);
        LoginResponse loginResponse = LoginResponse.builder().token(jwtToken).expiresIn(jwtService.getExpirationTime()).build();

        return ResponseEntity.ok(loginResponse);
    }
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
    @PostMapping("/add-seller")
    public ResponseEntity<User> addSeller(@RequestBody RegisterSellerDto seller){
        sellerService.addSeller(seller);
        User registeredUser = authenticationService.signupSellerByAdmin(seller);
        return ResponseEntity.ok(registeredUser);
    }

    @DeleteMapping("/remove/skill/{id}")
    public ResponseEntity<Void> deleteSkills(@PathVariable int id){
        skillsService.deleteSkill(id);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/remove/customer/{id}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable int id){
        customerService.deleteCustomer(id);
        authenticationService.deleteUserByCustomerId(id);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/remove/seller/{id}")
    public ResponseEntity<Void> deleteSeller(@PathVariable int id){
        sellerService.deleteSeller(id);
        authenticationService.deleteUserBySellerId(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/add-admin")
    public ResponseEntity<User> addAdmin(@RequestBody RegisterCustomerDto registerUserDto){
        User registeredUser = authenticationService.signupAdmin(registerUserDto);
        return ResponseEntity.ok(registeredUser);
    }

    @PutMapping("/{id}")
    public Skills updateSkill(@PathVariable int id, @RequestBody Skills skill) {
        return skillsService.updateSkill(id, skill);
    }
}
