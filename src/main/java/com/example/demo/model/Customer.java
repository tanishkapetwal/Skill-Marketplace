package com.example.demo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.List;


@Data
@AllArgsConstructor
@Entity
@Builder
public class Customer {

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public Customer() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = createdAt;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    private String name;
    @NotNull
    private String email;
    @NotNull
    @Pattern(regexp = "^\\+?[1-9][0-9]{7,14}$")
    private String phone;

    @NotBlank(message = "Password Cannot Be Empty")
    @Size(min=8,max=20,message = "Password must be between 8 and 10 characters")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).*$",

            message = "Password must contain at least one digit, one lowercase, one uppercase, and one special character"
    )
    private String password;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;


    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Orders> order;


}
