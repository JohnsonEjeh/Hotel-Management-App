package org.ejeh.hotelmanagementmvc.controller;

import jakarta.servlet.http.HttpSession;
import org.ejeh.hotelmanagementmvc.model.Hotel;
import org.ejeh.hotelmanagementmvc.model.Staff;
import org.ejeh.hotelmanagementmvc.service.HotelService;
import org.ejeh.hotelmanagementmvc.service.StaffService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/staff")
public class StaffController {
    private final StaffService staffService;
    private final HotelService hotelService;

    public StaffController(StaffService staffService, HotelService hotelService) {
        this.staffService = staffService;
        this.hotelService = hotelService;
    }

    @GetMapping
    public String getAllStaff(Model model, HttpSession session) {
        model.addAttribute("staffList", staffService.getAllStaff(session));
        return "staff/list";
    }

    @GetMapping("/sorted")
    public String getAllStaffSorted(Model model, HttpSession session) {
        model.addAttribute("staffList", staffService.getAllStaffSortedByDepartment(session));
        return "staff/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model, HttpSession session) {
        model.addAttribute("staff", new Staff());
        // Only show 3-star or lower hotels for new staff
        model.addAttribute("hotels", staffService.getEligibleHotelsForStaff(3, session));
        model.addAttribute("departments", List.of("Reception", "Cleaning", "Management", "Restaurant"));
        return "staff/add";
    }

    @PostMapping("/add")
    public String addStaff(@ModelAttribute Staff staff,
                           BindingResult result,
                           HttpSession session,
                           RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.staff", result);
            redirectAttributes.addFlashAttribute("staff", staff);
            return "redirect:/staff/add";
        }

        try {
            staffService.createStaff(staff, session);
            redirectAttributes.addFlashAttribute("success", "Staff added successfully");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/staff/add";
        }
        return "redirect:/staff";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id,
                               @RequestParam(required = false) Integer rating,
                               @RequestParam(required = false) Boolean refresh,
                               Model model,
                               HttpSession session) {
        Staff staff = (Staff) staffService.getStaffById(id, session);

        // Use the provided rating if available, otherwise use the staff's current rating
        int ratingToUse = rating != null ? rating : staff.getPerformanceRating();

        // Update staff rating if a new rating was provided
        if (rating != null && rating != staff.getPerformanceRating()) {
            staff.setPerformanceRating(rating);
        }

        model.addAttribute("staff", staff);
        model.addAttribute("hotels", staffService.getEligibleHotelsForStaff(ratingToUse, session));
        model.addAttribute("departments", List.of("Reception", "Cleaning", "Management", "Restaurant"));

        if (refresh != null && refresh) {
            model.addAttribute("success", "Hotel list refreshed based on current rating");
        }

        return "staff/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateStaff(@PathVariable Long id,
                              @ModelAttribute Staff staff,
                              @RequestParam(name="hotelName", required=false) String hotelName,
                              @RequestParam(name="starRating", required=false) Integer starRating,
                              @RequestParam(name="hotelId", required=false) String hotelId,
                              BindingResult result,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            result.getAllErrors().forEach(error ->
                    System.err.println("Validation error: " + error.toString()));
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.staff", result);
            redirectAttributes.addFlashAttribute("staff", staff);
            return "redirect:/staff/edit/" + id;
        }

        try {
            System.out.println("Attempting to update staff: " + staff);

            if(hotelName != null && starRating != null && hotelId != null){
                Hotel hotel = new Hotel(hotelId, hotelName, starRating);
                staff.setHotel(hotel);
            }

            Staff updatedStaff = staffService.updateStaff(id, staff, session);
            System.out.println("Successfully updated staff: " + updatedStaff);
            redirectAttributes.addFlashAttribute("success", "Staff updated successfully");
        } catch (IllegalArgumentException e) {
            System.err.println("Error updating staff: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/staff/edit/" + id;
        } catch (Exception e) {
            System.err.println("Unexpected error updating staff: " + e.getMessage());
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("error", "An unexpected error occurred");
            return "redirect:/staff/edit/" + id;
        }
        return "redirect:/staff";
    }

    @GetMapping("/delete/{id}")
    public String deleteStaff(@PathVariable Long id,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        staffService.deleteStaff(id, session);
        redirectAttributes.addFlashAttribute("success", "Staff deleted successfully");
        return "redirect:/staff";
    }

    @PostMapping("/{id}/rating")
    public String updateRating(@PathVariable Long id,
                               @RequestParam("performanceRating") int performanceRating,
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        try {
            Staff updatedStaff = staffService.updateStaffRating(id, performanceRating, session);

            // Check if we need to clear hotel assignment
            if (updatedStaff.getHotel() != null &&
                    performanceRating >= 4 &&
                    updatedStaff.getHotel().getStarRating() < 4) {
                // Automatically unassign from incompatible hotel
                updatedStaff.setHotel(null);
                staffService.updateStaff(id, updatedStaff, session);
                redirectAttributes.addFlashAttribute("warning",
                        "Staff was unassigned from hotel due to rating change");
            }

            redirectAttributes.addFlashAttribute("success", "Rating updated successfully");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/staff";
    }


}