package com.example.demo.skill.management.dto;

import com.example.demo.skill.management.constant.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SkillsListingDTO {

    private int id;
    private String title;

    private String description;

    private float price;

    private double time;
    private int avgRating;

    private String skillsName;

    private String skillsDescription;


    private Category skillsCategory;

    private String sellerUserName;
}
