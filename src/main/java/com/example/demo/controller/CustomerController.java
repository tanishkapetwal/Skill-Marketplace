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
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static java.time.LocalDate.*;


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
        System.out.println(createorderdto);
        service.createOrder(userId,listingId,createorderdto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/all-orders")
    public ResponseEntity<List<AllOrderResponse>> getallOrders(HttpServletRequest request){
        userId = getUserId(request);
        return new ResponseEntity<>(service.getallOrders(userId), HttpStatus.OK);
    }
    @PostMapping("/signup")
    public ResponseEntity<?> addCustomers(@RequestBody RegisterCustomerDto registerUserDto, HttpServletResponse response){
        User registeredUser = authenticationService.signup(registerUserDto);
        LoginUserDto loginUserDto= new LoginUserDto();
        loginUserDto.setEmail(registeredUser.getEmail());
        loginUserDto.setPassword(registerUserDto.getPassword());
        return authenticate(loginUserDto, response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginUserDto loginUserDto, HttpServletResponse response) {
        Authentication authentication = authenticationService.authenticate(loginUserDto);

        UserDetails authenticatedUser = (UserDetails) authentication.getPrincipal();

        int userId = authenticationService.fetchCustomerId(authenticatedUser);
        String jwtToken = jwtService.generateToken(authenticatedUser,userId);
        LoginResponse loginResponse = LoginResponse.builder().accessToken(jwtToken)
                                    .expiresIn(jwtService.getExpirationTime()).build();

        String refreshToken = jwtService.generateRefreshToken(authenticatedUser,userId);
        ResponseCookie cookie =  ResponseCookie.from("refreshToken", refreshToken)
                        .httpOnly(true).secure(false).path("/").maxAge(7*24*60*60).sameSite("None").build();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE,cookie.toString());

        return ResponseEntity.ok().headers(headers).body(loginResponse);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response, HttpServletRequest request){
        ResponseCookie cookie =  ResponseCookie.from("refreshToken", "")
                .httpOnly(true).secure(true).path("/").maxAge(0).sameSite("Lax").build();

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE,cookie.toString());

        return ResponseEntity.ok().headers(headers).build();

    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(HttpServletRequest request, HttpServletResponse response){
        String refreshToken = Arrays.stream(request.getCookies()).filter
                            (c->"refreshToken".equals(c.getName()))
                            .findFirst().map(Cookie::getValue).orElse(null);
        System.out.println("Refresh Token is: "+refreshToken);
        if(refreshToken==null ){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        if(jwtService.isTokenExpired(refreshToken)){

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String username = jwtService.extractUsername(refreshToken);
        int userId = jwtService.extractUserId(refreshToken);
        UserDetails user = userDetailsService.loadUserByUsername(username);
        String newAccessToken = jwtService.generateToken(user, userId);
        String newRefreshToken = jwtService.generateRefreshToken(user,userId);
        ResponseCookie cookie = ResponseCookie.from("refreshToken",newRefreshToken)
                .httpOnly(true).secure(true).path("/").sameSite("Lax").maxAge(7*24*60*60).build();
        HttpHeaders headers= new HttpHeaders();
        headers.add(HttpHeaders.SET_COOKIE,cookie.toString());
        return ResponseEntity.ok().headers(headers).body(Map.of("accessToken",newAccessToken));

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
