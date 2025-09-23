package com.example.demo.controller;


import com.example.demo.dto.*;
import com.example.demo.model.Customer;
import com.example.demo.model.Seller;
import com.example.demo.model.Skills;
import com.example.demo.model.User;
import com.example.demo.security.JWTService;
import com.example.demo.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
@RestController

@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;
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
    @GetMapping("/all-customers")
    public List<CustomerResponseDto> getCustomers(){
        return customerService.getCustomers();

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
        adminService.deleteUser(id);
        return ResponseEntity.ok().build();
    }
    @DeleteMapping("/remove/seller/{id}")
    public ResponseEntity<Void> deleteSeller(@PathVariable int id){
        adminService.deleteUser(id);

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
