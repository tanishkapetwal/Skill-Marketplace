package com.example.demo.customer.managment.rest.controller;

import com.example.demo.skill.management.dto.SkillsListingDTO;
import com.example.demo.system.configuration.dto.CustomerResponseDto;
import com.example.demo.system.configuration.security.JWTService;
import com.example.demo.system.configuration.service.impl.AuthenticationService;
import com.example.demo.customer.managment.service.CustomerService;
import com.example.demo.seller.management.service.SellerService;
import com.example.demo.skill.management.service.SkillsListingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    @Autowired
    private final AuthenticationService authenticationService;
    @Autowired
    private final JWTService jwtService;
    @Autowired
    private SkillsListingService skillsListingService;
    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private final CustomerService service;
    @Autowired
    private SellerService sellerService;

    int userId;

    public int getUserId(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.split(" ")[1];
        userId = jwtService.extractClaim(token, claims -> claims.get("id", Integer.class));
        return userId;
    }

    @GetMapping("/")
    public CustomerResponseDto getCustomerById(HttpServletRequest request) {
        userId = getUserId(request);
        System.out.println("customer Id"+userId);
        return service.getCustomerbyId(userId);
    }

//    @GetMapping("/skills")
//    public ResponseEntity<List<SkillsListingDTO>> getallskills() {
//        List<SkillsListingDTO> test = service.getallskills();
//        System.out.println(test);
//        return new ResponseEntity<>(test, HttpStatus.OK);
//    }
    @GetMapping("/skills")
    public Page<SkillsListingDTO> getallskills(@RequestParam int page) {

        return service.getallskills(page);
    }

    @GetMapping("/skills/{id}")
    public ResponseEntity<SkillsListingDTO> getallskillsbyId(@PathVariable Integer id) {
        return new ResponseEntity<>(service.getallskillsbyId(id), HttpStatus.OK);
    }

    @DeleteMapping(value = {"/delete"})
    public ResponseEntity<Void> deleteCustomer(HttpServletRequest request) {
        userId = getUserId(request);
        service.deleteCustomer(userId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/order/{orderId}/rate")
    public ResponseEntity<String> rateOrder(
            @PathVariable int orderId,
            @RequestParam int ratingValue,
            HttpServletRequest request) {

        int customerId = getUserId(request);
        service.saveOrder(orderId, customerId, ratingValue);
        return ResponseEntity.ok("Rating submitted successfully!");
    }

}
