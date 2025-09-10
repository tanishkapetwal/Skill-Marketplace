package com.example.demo.dto;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class CreateListingDTO {
    @NotNull
    private String title;           // not null constraint must be in DTO

    @NotNull
    private String description;  // not null constraint must be in DTO
    private float price;
    private double time;
}
