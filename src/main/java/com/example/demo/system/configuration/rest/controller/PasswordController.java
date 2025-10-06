package com.example.demo.system.configuration.rest.controller;

import com.example.demo.system.configuration.dto.LoginUserDto;
import com.example.demo.system.configuration.security.JWTService;
import com.example.demo.system.configuration.service.impl.ResetPasswordService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@Controller
@RequiredArgsConstructor
public class PasswordController {

    private final ResetPasswordService resetPasswordService;
    private final JWTService jwtService;
    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetSellerPassword(@RequestBody String email) {
        System.out.println(email);
        resetPasswordService.resetPasswordForCurrentUser(email);
        return ResponseEntity.ok().build();
    }
    @PostMapping("/set-password")
    public ResponseEntity<Void> setNewPassword(@RequestHeader("Authorization")String token, @RequestBody String password) {
       String authToken = token.split(" ")[1];
        String email = jwtService.extractUsername(authToken);
//        String newPass = loginUserDto.getPassword();
        resetPasswordService.setNewPassword(email,password);
        return ResponseEntity.ok().build();
    }
}
