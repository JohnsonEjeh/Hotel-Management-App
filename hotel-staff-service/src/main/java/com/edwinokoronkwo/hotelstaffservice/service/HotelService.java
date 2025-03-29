package com.edwinokoronkwo.hotelstaffservice.service;

import com.edwinokoronkwo.hotelstaffservice.model.Hotel;
import java.util.List;

public interface HotelService {
    List<Hotel> getAllHotels();
    Hotel getHotelById(String hotelId);
    Hotel createHotel(Hotel hotel);
    Hotel updateHotel(String hotelId, Hotel hotel);
    void deleteHotel(String hotelId);
}