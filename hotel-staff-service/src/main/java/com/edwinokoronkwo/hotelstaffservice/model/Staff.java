package com.edwinokoronkwo.hotelstaffservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
public class Staff {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "staff_id")
    private Long staffId;

    @NotBlank(message = "Staff name is mandatory")
    @Column(name = "staff_name")
    private String staffName;

    @ManyToOne
    @JoinColumn(name = "hotel_id")
    private Hotel hotel;

    @Column(name = "department")
    private String department;

    @Column(name = "performance_rating")
    private int performanceRating = 3; // Default performance rating set to 3
}