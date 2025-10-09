package com.example.demo.skill.management.model;

import com.example.demo.order.management.model.Orders;
import com.example.demo.seller.management.model.Seller;
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
public class SkillsListing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @NotNull
    private String title;
    @NotNull
    private String description;
    @NotNull     //constraint
    private float price;
    @NotNull          //constraint
    private double time;
    @Column
    private Double avgRating;
    @Column(nullable = false)
    private int ratingCount = 0;   // total number of ratings received


    @ManyToOne
    @JoinColumn(name = "skills_id")
    private Skills skills;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;


    @OneToMany(mappedBy = "skillslisting", cascade = CascadeType.ALL)
    private List<Orders> orders;

}
