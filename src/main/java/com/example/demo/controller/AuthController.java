package com.example.demo.controller;

import com.example.demo.dto.LoginResponse;
import com.example.demo.dto.LoginUserDto;
import com.example.demo.dto.RegisterCustomerDto;
import com.example.demo.dto.RegisterSellerDto;
import com.example.demo.model.User;
import com.example.demo.model.type.Role;
import com.example.demo.repository.UserRepo;
import com.example.demo.security.JWTService;
import com.example.demo.service.AuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class AuthController {

    private final AuthenticationService authenticationService;
    private final UserRepo userRepo;
    private final JWTService jwtService;
    private final UserDetailsService userDetailsService;
    @PostMapping("/customer/signup")
    public ResponseEntity<?> addCustomers(@RequestBody RegisterCustomerDto registerUserDto, HttpServletResponse response){
        User registeredUser = authenticationService.signup(registerUserDto);
        LoginUserDto loginUserDto= new LoginUserDto();
        loginUserDto.setEmail(registeredUser.getEmail());
        loginUserDto.setPassword(registerUserDto.getPassword());
        return authenticate(loginUserDto, response);
    }
    @PostMapping("/seller/signup")
    public ResponseEntity<?> addSeller(@RequestBody RegisterSellerDto registerSellerDto,HttpServletResponse response) {
        User registeredSeller = authenticationService.signupSeller(registerSellerDto);
        LoginUserDto loginUserDto= new LoginUserDto();
        loginUserDto.setEmail(registeredSeller.getEmail());
        loginUserDto.setPassword(registerSellerDto.getPassword());
        return authenticate(loginUserDto, response);

    }
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto,HttpServletResponse response) {
        Authentication authentication = authenticationService.authenticate(loginUserDto);
        UserDetails authenticatedUser = (UserDetails) authentication.getPrincipal();
        int userId=authenticationService.findUserId(authenticatedUser);

        String jwtToken = jwtService.generateToken(authenticatedUser,userId);
        LoginResponse loginResponse = LoginResponse.builder().accessToken(jwtToken).role(authenticationService.getUser(authenticatedUser.getUsername()))
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
        System.out.println("in logout");
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
}
