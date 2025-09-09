package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.model.Customer;
import com.example.demo.model.Orders;
import com.example.demo.model.Skills;
import com.example.demo.security.JWTService;
import com.example.demo.service.AuthenticationService;
import com.example.demo.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customer")
public class CustomerController {

    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private JWTService jwtService;

    @Autowired
    private CustomerService service;

    @GetMapping("/{id}")
    public CustomerResponseDto getCustomerById(@PathVariable Integer id){
        return service.getCustomerbyId(id);
    }

    @GetMapping("/skills")
    public ResponseEntity<List<Skills>> getallskills(){
        return new ResponseEntity<>(service.getallskills(), HttpStatus.OK);
    }

    @GetMapping("/skills/{id}")
    public ResponseEntity<Skills> getallskillsbyId(@PathVariable Integer id){
        return new ResponseEntity<>(service.getallskillsbyId(id), HttpStatus.OK);
    }


    @PostMapping("/{id}/order/{listingId}")
    public ResponseEntity<Orders> createOrder(@PathVariable int id, @PathVariable int listingId, @RequestBody CreateOrderDTO createorderdto){
        service.createOrder(id,listingId,createorderdto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/all-orders")
    public ResponseEntity<List<AllOrderResponse>> getallOrders(@PathVariable int id){
        return new ResponseEntity<>(service.getallOrders(id), HttpStatus.OK);
    }
    @PostMapping("/signup")
    public ResponseEntity<Customer> addCustomers(@RequestBody RegisterUserDto registerUserDto){
        Customer registeredUser = authenticationService.signup(registerUserDto);

        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        Authentication authentication = authenticationService.authenticate(loginUserDto);

        UserDetails authenticatedUser = (UserDetails) authentication.getPrincipal();

//      if(authenticatedUser.getAuthorities().equals("Role_CUSTOMER"))
            int userId = authenticationService.fetchCustomerId(authenticatedUser);
        String jwtToken = jwtService.generateToken(authenticatedUser,userId);
        LoginResponse loginResponse = LoginResponse.builder().token(jwtToken).expiresIn(jwtService.getExpirationTime()).build();

        return ResponseEntity.ok(loginResponse);
    }

    @DeleteMapping(value = {"/delete/{id}"})
    public ResponseEntity<Void>  deleteCustomer(@PathVariable Integer id){
            service.deleteCustomer(id);
            return ResponseEntity.ok().build();

    }
}
