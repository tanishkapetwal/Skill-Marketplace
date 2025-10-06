package com.example.demo.admin.management.controller;


import com.example.demo.admin.management.mapper.AdminMapper;
import com.example.demo.admin.management.service.AdminService;
import com.example.demo.customer.managment.service.CustomerService;
import com.example.demo.skill.management.model.Skills;
import com.example.demo.seller.management.service.SellerService;
import com.example.demo.skill.management.dto.SkillsResponseDTO;
import com.example.demo.skill.management.service.SkillsService;
import com.example.demo.system.configuration.dto.CustomerResponseDto;
import com.example.demo.system.configuration.dto.RegisterCustomerDto;
import com.example.demo.system.configuration.dto.RegisterSellerDto;
import com.example.demo.system.configuration.dto.SellerResponseDto;
import com.example.demo.system.configuration.model.User;
import com.example.demo.system.configuration.security.JWTService;
import com.example.demo.system.configuration.service.impl.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "http://localhost:4200")
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
    private final AdminMapper adminMapper;
    @PostMapping("/add-skills")
    public ResponseEntity<Skills> addSkills(@RequestBody Skills skill){
        return new ResponseEntity<>(skillsService.addSkills(skill), HttpStatus.OK);
    }
    @PostMapping("/add-customer")
    public ResponseEntity<User> addCustomers(@Valid @RequestBody RegisterCustomerDto customer){
        return ResponseEntity.ok(customerService.addCustomers(adminMapper.registerCustomerDtoToCustomer(customer)));
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
    public ResponseEntity<User> addSeller(@Valid @RequestBody RegisterSellerDto seller){
        return ResponseEntity.ok(sellerService.addSeller(adminMapper.registerSellerDtoToSeller(seller)));
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
        return ResponseEntity.ok(adminService.signupAdmin(  adminMapper.registerAdminDtoToAdmin(registerUserDto)));
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
