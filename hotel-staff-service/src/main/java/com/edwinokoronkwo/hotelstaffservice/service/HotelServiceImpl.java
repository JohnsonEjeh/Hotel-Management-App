package com.edwinokoronkwo.hotelstaffservice.service;

import com.edwinokoronkwo.hotelstaffservice.model.Hotel;
import com.edwinokoronkwo.hotelstaffservice.repository.HotelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HotelServiceImpl implements HotelService {

    private final HotelRepository hotelRepository;

    @Override
    public List<Hotel> getAllHotels() {
        return hotelRepository.findAll();
    }

    @Override
    public Hotel getHotelById(String hotelId) {
        return hotelRepository.findById(hotelId).orElse(null);
    }

    @Override
    public Hotel createHotel(Hotel hotel) {
        return hotelRepository.save(hotel);
    }

    @Override
    public Hotel updateHotel(String hotelId, Hotel hotel) {
        if (hotelRepository.existsById(hotelId)) {
            hotel.setHotelId(hotelId);
            return hotelRepository.save(hotel);
        }
        return null;
    }

    @Override
    public void deleteHotel(String hotelId) {
        hotelRepository.deleteById(hotelId);
    }
}