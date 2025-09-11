package com.example.demo.model;

import com.example.demo.model.type.Category;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Skills {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    private String name;
    @NotNull
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Category category;

    @OneToMany(mappedBy = "skills", cascade = CascadeType.ALL)
    private List<SkillsListing> skillsListing;

}
