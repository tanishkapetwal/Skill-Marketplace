package com.example.demo.model;

import com.example.demo.model.type.Status;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;



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
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "skills_listing_id") // This is the name u give to column in this table referencing other table's id
    private SkillsListing skillslisting;
}

