package com.example.demo.dto;

import com.example.demo.model.type.Category;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SkillsListingDTO {

    private String title;

    private String description;

    private float price;

    private double time;
    private int skillRating;

    private String skillsName;

    private String skillsDescription;


    private Category skillsCategory;

    private String sellerUserName;
}
