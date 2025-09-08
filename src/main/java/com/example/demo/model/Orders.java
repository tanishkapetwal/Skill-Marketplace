package com.example.demo.model;

import com.example.demo.model.type.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Orders {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private LocalDateTime appointmentStart;
    private LocalDateTime appointmentEnd;
    private int orderRating;
    private Status status;
    private LocalDate orderDate;

    @ManyToOne
    @JoinColumn(name = "cust_id")
    private Customer customer;

    @ManyToMany (mappedBy = "orders", cascade = CascadeType.ALL)
    private List<SkillsListing> skillsListing;

}

