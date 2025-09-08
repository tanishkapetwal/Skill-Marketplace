package com.example.demo.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class SkillsListing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String description;
    private float price;
    private double time;
    private int skillRating;

    @ManyToOne
    @JoinColumn(name = "skills_id")
    private Skills skills;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;

    @ManyToMany
    @JoinTable(
            name = "orders_skillsListing",
            joinColumns = @JoinColumn(name = "listing_id"),
            inverseJoinColumns = @JoinColumn(name = "order_id")
    )
    private List<Orders> orders;
}
