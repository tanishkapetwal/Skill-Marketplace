package com.example.demo.skill.management.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateListingDTO {
    @NotNull
    private String title;

    @NotNull
    private String description;
    @NotNull
    @Min(value = 1, message = "Price cannot be negative")
    private float price;
    @NotNull
    @Min(value = 1, message = "Time cannot be negative")
    private double time;

}

