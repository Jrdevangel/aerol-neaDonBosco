package com.flightDB.DBApp.controller;

import com.flightDB.DBApp.model.Flight;
import com.flightDB.DBApp.model.FlightImage;
import com.flightDB.DBApp.service.FlightImageService;
import com.flightDB.DBApp.service.FlightsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/image")
@CrossOrigin
public class FlightImageController {

    @Autowired
    private FlightImageService flightImageService;

    @Autowired
    private FlightsService flightService;

    // Отримання зображень за ID рейсу
    @GetMapping("/flight/{flightId}")
    public List<FlightImage> getImagesByFlightId(@PathVariable Long flightId) {
        return flightImageService.getImagesByFlightId(flightId);
    }

    // Завантаження зображення для рейсу
    @PostMapping("/create/{flightId}")
    public ResponseEntity<?> uploadFlightImage(
            @PathVariable Long flightId,
            @RequestParam("file") MultipartFile file) {
        try {
            // Перевірка наявності файлу
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("File cannot be empty.");
            }

            // Отримання рейсу за ID
            Flight flight = flightService.getFlightById(flightId);
            if (flight == null) {
                return ResponseEntity.badRequest().body("Flight not found.");
            }

            // Збереження файлу
            byte[] imageBytes = file.getBytes();
            FlightImage flightImage = new FlightImage();
            flightImage.setImageData(imageBytes); // Зберігаємо зображення як байти
            flightImage.setFlight(flight);

            // Збереження зображення
            flightImageService.saveFlightImage(flightImage);

            return ResponseEntity.ok("Image uploaded successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error saving image: " + e.getMessage());
        }
    }

    // Видалення зображення за ID
    @DeleteMapping("/delete/{id}")
    public void deleteFlightImage(@PathVariable Long id) {
        flightImageService.deleteFlightImage(id);
    }
}
