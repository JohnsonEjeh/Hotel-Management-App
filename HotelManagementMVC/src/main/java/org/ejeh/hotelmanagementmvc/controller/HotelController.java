package org.ejeh.hotelmanagementmvc.controller;

import jakarta.servlet.http.HttpSession;
import org.ejeh.hotelmanagementmvc.model.Hotel;
import org.ejeh.hotelmanagementmvc.service.HotelService;
import org.ejeh.hotelmanagementmvc.service.StaffService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/hotels")
public class HotelController {

    private final HotelService hotelService;
    private final StaffService staffService;

    public HotelController(HotelService hotelService, StaffService staffService) {
        this.hotelService = hotelService;
        this.staffService = staffService;
    }

    @GetMapping
    public String getAllHotels(Model model, HttpSession session) {
        List<Hotel> hotels = hotelService.getAllHotels(session);
        model.addAttribute("hotels", hotels);
        return "hotels/list";
    }

    @GetMapping("/add")
    public String showAddHotelForm(Model model) {
        model.addAttribute("hotel", new Hotel());
        return "hotels/add";
    }

    @PostMapping("/add")
    public String addHotel(@Valid @ModelAttribute("hotel") Hotel hotel,
                           HttpSession session,
                           BindingResult result) {
        if (result.hasErrors()) {
            return "hotels/add";
        }
        hotelService.createHotel(hotel, session);
        return "redirect:/hotels";
    }

    @GetMapping("/edit/{hotelId}")
    public String showEditHotelForm(@PathVariable String hotelId,HttpSession session, Model model) {
        Hotel hotel = hotelService.getHotelById(hotelId, session);
        model.addAttribute("hotel", hotel);
        return "hotels/edit";
    }

    @PostMapping("/edit/{hotelId}")
    public String updateHotel(@PathVariable String hotelId,
                              @Valid @ModelAttribute("hotel") Hotel hotel,
                              HttpSession session,
                              BindingResult result) {
        if (result.hasErrors()) {
            return "hotels/edit";
        }
        hotelService.updateHotel(hotelId, hotel, session);
        return "redirect:/hotels";
    }

    @GetMapping("/delete/{hotelId}")
    public String deleteHotel(@PathVariable String hotelId,
                              HttpSession session,
                              RedirectAttributes redirectAttributes) {
        try {
            hotelService.deleteHotel(hotelId, session);
            redirectAttributes.addFlashAttribute("success", "Hotel deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "Failed to delete hotel: " + e.getMessage());
        }
        return "redirect:/hotels";
    }

    @GetMapping("/hotels-by-rating")
    @ResponseBody
    public List<Hotel> getHotelsByRating(@RequestParam int rating, HttpSession session) {
        List<Hotel> allHotels = hotelService.getAllHotels(session);

        // Filter hotels based on staff rating
        return allHotels.stream()
                .filter(hotel -> (rating >= 4 && hotel.getStarRating() >= 4) ||
                        (rating < 4 && hotel.getStarRating() <= 3))
                .collect(Collectors.toList());
    }

    @GetMapping("/eligible-hotels/{rating}")
    @ResponseBody
    public List<Hotel> getEligibleHotels(@PathVariable int rating, HttpSession session) {
        return staffService.getEligibleHotelsForStaff(rating, session);
    }

}
