package org.ejeh.hotelmanagementmvc.model;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import java.util.List;
public class Staff {
    private Long staffId;

    @NotBlank(message = "Staff name is required")
    private String staffName;

    @NotNull(message = "Department is required")
    private String department;

    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    private int performanceRating = 3;

    private Hotel hotel;

    private String hotelId; // Add hotelId as a string field

    // Getters and Setters
    public Long getStaffId() { return staffId; }
    public void setStaffId(Long staffId) { this.staffId = staffId; }
    public String getStaffName() { return staffName; }
    public void setStaffName(String staffName) { this.staffName = staffName; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public int getPerformanceRating() { return performanceRating; }
    public void setPerformanceRating(int performanceRating) { this.performanceRating = performanceRating; }

    public void setHotel(Hotel hotel) { this.hotel = hotel; }
    public Hotel getHotel() {
        return hotel != null ? hotel : new Hotel(); // Return empty hotel instead of null
    }

    public String getHotelId() {
        return hotelId;
    }

    public void setHotelId(String hotelId) {
        this.hotelId = hotelId;
    }

    public void validateHotelAssignment(List<Hotel> allHotels) {
        if (hotelId != null && !hotelId.isEmpty()) {
            // Find the complete hotel details
            Hotel assignedHotel = allHotels.stream()
                    .filter(h -> h.getHotelId().equals(hotelId))
                    .findFirst()
                    .orElse(null);

            if (assignedHotel != null) {
                if (performanceRating >= 4 && assignedHotel.getStarRating() < 4) {
                    throw new IllegalArgumentException(
                            "Staff with rating 4+ can only be assigned to 4+ star hotels");
                }
                if (performanceRating < 4 && assignedHotel.getStarRating() > 3) {
                    throw new IllegalArgumentException(
                            "Staff with rating below 4 cannot be assigned to 4+ star hotels");
                }
                //Assign Hotel object.
                this.hotel = assignedHotel;
            }
        }
    }
}