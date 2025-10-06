package com.example.demo.system.configuration.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class LoginResponse {
    private String accessToken;
    private long expiresIn;
    private String role;

    public String getToken() {
        return accessToken;
    }
}
