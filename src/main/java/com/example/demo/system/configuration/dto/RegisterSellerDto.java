package com.example.demo.system.configuration.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterSellerDto {

    @NotNull
    private String name;
    @NotNull

    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$",
            message = "Invalid email format")
    private String email;
    @NotNull
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&+=])(?=\\S+$).{8,20}$",
            message = "Password must be 8-20 characters long and include at least one digit, one lowercase letter, one uppercase letter, and one special character.")
    private String password;
    @NotNull
    @Pattern(regexp = "\\d{10}", message = "Phone number must be 10 digits")
    private String phone;
    private String bio;
}
