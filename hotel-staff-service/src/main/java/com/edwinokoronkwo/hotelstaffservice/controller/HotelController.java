package com.edwinokoronkwo.hotelstaffservice.controller;

import com.edwinokoronkwo.hotelstaffservice.model.Hotel;
import com.edwinokoronkwo.hotelstaffservice.service.HotelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/hotels")
@RequiredArgsConstructor
public class HotelController {

    private final HotelService hotelService;

    @GetMapping
    public ResponseEntity<List<Hotel>> getAllHotels() {
        return ResponseEntity.ok(hotelService.getAllHotels());
    }

    @GetMapping("/{hotelId}")
    public ResponseEntity<Hotel> getHotelById(@PathVariable String hotelId) {
        Hotel hotel = hotelService.getHotelById(hotelId);
        if (hotel != null) {
            return ResponseEntity.ok(hotel);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Hotel> createHotel(@Valid @RequestBody Hotel hotel) {
        return ResponseEntity.status(HttpStatus.CREATED).body(hotelService.createHotel(hotel));
    }

    @PutMapping("/{hotelId}")
    public ResponseEntity<Hotel> updateHotel(@PathVariable String hotelId, @Valid @RequestBody Hotel hotel) {
        Hotel updatedHotel = hotelService.updateHotel(hotelId, hotel);
        if (updatedHotel != null) {
            return ResponseEntity.ok(updatedHotel);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{hotelId}")
    public ResponseEntity<String> deleteHotel(@PathVariable String hotelId) {
        hotelService.deleteHotel(hotelId);
        return ResponseEntity.ok("Hotel deleted successfully."); // Return a success message
    }
}