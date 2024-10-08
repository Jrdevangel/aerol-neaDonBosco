package com.flightDB.DBApp.service;


import com.flightDB.DBApp.model.Flight;
import com.flightDB.DBApp.model.FlightImage;
import com.flightDB.DBApp.repository.IFlightImageRepository;
import com.flightDB.DBApp.repository.IFlightRepository;
import com.flightDB.DBApp.service.FlightImageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FlightImageServiceTest {

    @InjectMocks
    private FlightImageService flightImageService;

    @Mock
    private IFlightImageRepository flightImageRepository;

    @Mock
    private IFlightRepository flightRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getImagesByFlightId_ShouldReturnImages() {
        Long flightId = 1L;
        List<FlightImage> images = new ArrayList<>();
        images.add(new FlightImage(new byte[]{}, new Flight()));

        when(flightImageRepository.findByFlightId(flightId)).thenReturn(images);

        List<FlightImage> response = flightImageService.getImagesByFlightId(flightId);

        assertNotNull(response);
        assertEquals(1, response.size());
        verify(flightImageRepository, times(1)).findByFlightId(flightId);
    }

    @Test
    void getById_ShouldReturnFlightImage() {
        Long imageId = 1L;
        FlightImage flightImage = new FlightImage(new byte[]{}, new Flight());

        when(flightImageRepository.getReferenceById(imageId)).thenReturn(flightImage);

        FlightImage response = flightImageService.getById(imageId);

        assertNotNull(response);
        assertEquals(flightImage, response);
        verify(flightImageRepository, times(1)).getReferenceById(imageId);
    }

    @Test
    void createFlightImage_validData_success() {
        String validBase64 = "iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAACklEQVR4nGMAAQAABQABDQottAAAAABJRU5ErkJggg==";
        Flight mockFlight = new Flight();
        FlightImage expectedFlightImage = new FlightImage(Base64.getDecoder().decode(validBase64), mockFlight);

        when(flightImageRepository.save(any(FlightImage.class))).thenReturn(expectedFlightImage);

        FlightImage result = flightImageService.createFlightImage(validBase64, mockFlight);

        assertNotNull(result);
        assertEquals(expectedFlightImage, result);
        verify(flightImageRepository, times(1)).save(any(FlightImage.class));
    }

    @Test
    void createFlightImage_invalidBase64_throwsException() {
        String invalidBase64 = "invalid_base64_string";
        Flight mockFlight = new Flight();

        assertThrows(IllegalArgumentException.class, () ->
                flightImageService.createFlightImage(invalidBase64, mockFlight)
        );
        verify(flightImageRepository, never()).save(any(FlightImage.class));
    }

    @Test
    void createFlightImage_emptyImageData_throwsException() {
        String emptyBase64 = "";
        Flight mockFlight = new Flight();

        assertThrows(IllegalArgumentException.class, () ->
                flightImageService.createFlightImage(emptyBase64, mockFlight)
        );
        verify(flightImageRepository, never()).save(any(FlightImage.class));
    }

    @Test
    void deleteFlightImage_ShouldCallDelete() {
        Long imageId = 1L;
        flightImageService.deleteFlightImage(imageId);

        verify(flightImageRepository, times(1)).deleteById(imageId);
    }

    @Test
    void saveFlightImage_ShouldSaveImage() {
        FlightImage flightImage = new FlightImage(new byte[]{}, new Flight());
        when(flightImageRepository.save(flightImage)).thenReturn(flightImage);

        FlightImage response = flightImageService.saveFlightImage(flightImage);

        assertNotNull(response);
        verify(flightImageRepository, times(1)).save(flightImage);
    }
}
