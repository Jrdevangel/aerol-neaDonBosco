package com.flightDB.DBApp.service;

import com.flightDB.DBApp.model.Flight;
import com.flightDB.DBApp.model.FlightImage;
import com.flightDB.DBApp.repository.IFlightImageRepository;
import com.flightDB.DBApp.repository.IFlightRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlightImageService {

    @Autowired
    private IFlightImageRepository flightImageRepository;

    @Autowired
    private IFlightRepository flightRepository;

    // Отримання зображень за ID рейсу
    public List<FlightImage> getImagesByFlightId(Long flightId) {
        return flightImageRepository.findByFlightId(flightId);
    }

    // Створення зображення на основі Base64
    public FlightImage createFlightImage(String imageDataBase64, Flight flight) {
        byte[] imageData;
        try {
            imageData = java.util.Base64.getDecoder().decode(imageDataBase64);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid Base64 image data", e);
        }

        if (imageData == null || imageData.length == 0) {
            throw new IllegalArgumentException("Image data is invalid.");
        }

        FlightImage flightImage = new FlightImage(imageData, flight);
        return flightImageRepository.save(flightImage);
    }

    // Видалення зображення
    public void deleteFlightImage(Long id) {
        flightImageRepository.deleteById(id);
    }

    @Transactional
    public FlightImage saveFlightImage(FlightImage flightImage) {
        return flightImageRepository.save(flightImage);
    }
}
