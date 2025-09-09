package com.example.demo.dto;

import lombok.Builder;
import lombok.Data;

@Builder
public class LoginResponse {
    private String token;

    private long expiresIn;

    public String getToken() {
        return token;
    }
}
