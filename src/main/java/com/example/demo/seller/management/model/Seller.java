package com.example.demo.seller.management.model;

import com.example.demo.skill.management.model.SkillsListing;
import com.example.demo.system.configuration.model.User;
import jakarta.persistence.*;
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
public class Seller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    private String bio;
    private int rating;
    @Column
    private Double avgRating;
    @Column(nullable = false)
    private int ratingCount = 0;   // total number of ratings received


    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL)
    private List<SkillsListing> skillsListing;



}

