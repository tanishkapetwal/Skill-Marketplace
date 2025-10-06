package com.example.demo.skill.management.dto;

import com.example.demo.skill.management.constant.Category;
import lombok.Data;


@Data
public class SkillsResponseDTO {

    private int id;
    private String name;
    private String description;
    private Category category;
}
