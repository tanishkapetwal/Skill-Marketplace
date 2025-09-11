package com.example.demo.dto;

import com.example.demo.model.type.Category;
import lombok.Data;


@Data
public class SkillsResponseDTO {

    private int id;
    private String name;
    private String description;
    private Category category;
}
