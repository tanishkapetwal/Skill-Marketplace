package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.model.Orders;
import com.example.demo.model.Skills;
import com.example.demo.model.SkillsListing;
import com.example.demo.model.User;
import com.example.demo.security.JWTService;
import com.example.demo.service.AuthenticationService;
import com.example.demo.service.CustomerService;
import com.example.demo.service.SkillsListingService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

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
    private final CustomerService service;

    int userId;
    public int getUserId(HttpServletRequest request){
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.split(" ")[1];
        userId = jwtService.extractClaim(token, claims -> claims.get("id", Integer.class));
       return userId;
    }
    @GetMapping("/")
    public CustomerResponseDto getCustomerById(HttpServletRequest request){
       userId = getUserId(request);
        return service.getCustomerbyId(userId);
    }

    @GetMapping("/skills")
    public ResponseEntity<List<SkillsListingDTO>> getallskills(){
        return new ResponseEntity<>(service.getallskills(), HttpStatus.OK);
    }

    @GetMapping("/skills/{id}")
    public ResponseEntity<SkillsListingDTO> getallskillsbyId(@PathVariable Integer id){
        return new ResponseEntity<>(service.getallskillsbyId(id), HttpStatus.OK);
    }


    @PostMapping("/order/{listingId}")
    public ResponseEntity<Orders> createOrder(@PathVariable int listingId, @RequestBody CreateOrderDTO createorderdto, HttpServletRequest request){
        userId = getUserId(request);
        service.createOrder(userId,listingId,createorderdto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all-orders")
    public ResponseEntity<List<AllOrderResponse>> getallOrders(HttpServletRequest request){
        userId = getUserId(request);
        return new ResponseEntity<>(service.getallOrders(userId), HttpStatus.OK);
    }
    @PostMapping("/signup")
    public ResponseEntity<LoginResponse> addCustomers(@RequestBody RegisterCustomerDto registerUserDto){
        User registeredUser = authenticationService.signup(registerUserDto);
        LoginUserDto loginUserDto= new LoginUserDto();
        loginUserDto.setEmail(registeredUser.getEmail());
        loginUserDto.setPassword(registerUserDto.getPassword());
        return authenticate(loginUserDto);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        Authentication authentication = authenticationService.authenticate(loginUserDto);

        UserDetails authenticatedUser = (UserDetails) authentication.getPrincipal();

        int userId = authenticationService.fetchUserId(authenticatedUser);
        String jwtToken = jwtService.generateToken(authenticatedUser,userId);
        LoginResponse loginResponse = LoginResponse.builder().token(jwtToken).expiresIn(jwtService.getExpirationTime()).build();

        return ResponseEntity.ok(loginResponse);
    }

    @DeleteMapping(value = {"/delete"})
    public ResponseEntity<Void>  deleteCustomer(HttpServletRequest request){
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
