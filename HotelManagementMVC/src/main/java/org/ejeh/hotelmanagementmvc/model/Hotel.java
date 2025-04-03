package org.ejeh.hotelmanagementmvc.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class Hotel {
    @Pattern(regexp = "^[A-Z][a-z]{3}\\d{4}$", message = "Hotel ID must be in the format 'Xxxx1234'")
    private String hotelId;

    @NotBlank(message = "Hotel name is mandatory")
    private String hotelName;

    private int starRating;

    // Constructors, getters, and setters
    public Hotel() {
    }

    public Hotel(String hotelId, String hotelName, int starRating) {
        this.hotelId = hotelId;
        this.hotelName = hotelName;
        this.starRating = starRating;
    }

    // Getters and setters
    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public int getStarRating() {
        return starRating;
    }

    public void setStarRating(int starRating) {
        this.starRating = starRating;
    }


}