package com.edwinokoronkwo.hotelstaffservice;

import com.edwinokoronkwo.hotelstaffservice.model.Hotel;
import com.edwinokoronkwo.hotelstaffservice.repository.HotelRepository;
import com.edwinokoronkwo.hotelstaffservice.service.HotelServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class HotelServiceImplTest {

    @Mock
    private HotelRepository hotelRepository;

    @InjectMocks
    private HotelServiceImpl hotelService;

    @Test
    void getAllHotels_shouldReturnAllHotels() {
        // Arrange
        List<Hotel> hotels = Arrays.asList(new Hotel(), new Hotel());
        when(hotelRepository.findAll()).thenReturn(hotels);

        // Act
        List<Hotel> result = hotelService.getAllHotels();

        // Assert
        assertEquals(2, result.size());
        verify(hotelRepository, times(1)).findAll();
    }

    @Test
    void getHotelById_shouldReturnHotel_whenHotelExists() {
        // Arrange
        String hotelId = "hotel1";
        Hotel hotel = new Hotel();
        hotel.setHotelId(hotelId);
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.of(hotel));

        // Act
        Hotel result = hotelService.getHotelById(hotelId);

        // Assert
        assertEquals(hotelId, result.getHotelId());
        verify(hotelRepository, times(1)).findById(hotelId);
    }

    @Test
    void getHotelById_shouldReturnNull_whenHotelDoesNotExist() {
        // Arrange
        String hotelId = "hotel1";
        when(hotelRepository.findById(hotelId)).thenReturn(Optional.empty());

        // Act
        Hotel result = hotelService.getHotelById(hotelId);

        // Assert
        assertNull(result);
        verify(hotelRepository, times(1)).findById(hotelId);
    }

    @Test
    void createHotel_shouldCreateHotel() {
        // Arrange
        Hotel hotel = new Hotel();
        when(hotelRepository.save(hotel)).thenReturn(hotel);

        // Act
        Hotel result = hotelService.createHotel(hotel);

        // Assert
        assertNotNull(result);
        verify(hotelRepository, times(1)).save(hotel);
    }

    @Test
    void updateHotel_shouldUpdateHotel_whenHotelExists() {
        // Arrange
        String hotelId = "hotel1";
        Hotel hotel = new Hotel();
        hotel.setHotelId(hotelId);
        when(hotelRepository.existsById(hotelId)).thenReturn(true);
        when(hotelRepository.save(hotel)).thenReturn(hotel);

        // Act
        Hotel result = hotelService.updateHotel(hotelId, hotel);

        // Assert
        assertEquals(hotelId, result.getHotelId());
        verify(hotelRepository, times(1)).existsById(hotelId);
        verify(hotelRepository, times(1)).save(hotel);
    }

    @Test
    void updateHotel_shouldReturnNull_whenHotelDoesNotExist() {
        // Arrange
        String hotelId = "hotel1";
        Hotel hotel = new Hotel();
        when(hotelRepository.existsById(hotelId)).thenReturn(false);

        // Act
        Hotel result = hotelService.updateHotel(hotelId, hotel);

        // Assert
        assertNull(result);
        verify(hotelRepository, times(1)).existsById(hotelId);
        verify(hotelRepository, never()).save(hotel);
    }

    @Test
    void deleteHotel_shouldDeleteHotel() {
        // Arrange
        String hotelId = "hotel1";

        // Act
        hotelService.deleteHotel(hotelId);

        // Assert
        verify(hotelRepository, times(1)).deleteById(hotelId);
    }
}