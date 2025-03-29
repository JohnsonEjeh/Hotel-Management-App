package com.edwinokoronkwo.hotelstaffservice.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor // Added NoArgsConstructor
public class Hotel {

    @Id
    @Column(name = "hotel_id")
    @Pattern(regexp = "^[A-Z][a-z]{3}\\d{4}$", message = "Hotel ID must be in the format 'Xxxx1234'")
    private String hotelId;

    @NotBlank(message = "Hotel name is mandatory")
    @Column(name = "hotel_name")
    private String hotelName;

    @Column(name = "star_rating")
    private int starRating;
}
