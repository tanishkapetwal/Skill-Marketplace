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
    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginUserDto loginUserDto, HttpServletResponse response) {
        Authentication authentication = authenticationService.authenticate(loginUserDto);

        UserDetails authenticatedUser = (UserDetails) authentication.getPrincipal();

        int userId = authenticationService.fetchAdminId(authenticatedUser);
        String jwtToken = jwtService.generateToken(authenticatedUser,userId);
        LoginResponse loginResponse = LoginResponse.builder().accessToken(jwtToken)
                .expiresIn(jwtService.getExpirationTime()).build();

        String refreshToken = jwtService.generateRefreshToken(authenticatedUser,userId);
        ResponseCookie cookie =  ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true).secure(true).path("/").maxAge(7*24*60*60).sameSite("Lax").build();

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
