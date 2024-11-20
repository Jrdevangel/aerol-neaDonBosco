package com.flightDB.DBApp.controller;

import com.flightDB.DBApp.dtos.SeatsWithPriceDTO;
import com.flightDB.DBApp.dtos.request.FlightDataToBuyDTO;
import com.flightDB.DBApp.dtos.request.RequestBoughtDataDTO;
import com.flightDB.DBApp.dtos.request.SeatDTO;
import com.flightDB.DBApp.dtos.response.ResponseToConfirmDTO;
import com.flightDB.DBApp.dtos.response.SeatAndPlaneDTO;
import com.flightDB.DBApp.model.Seats;
import com.flightDB.DBApp.service.SeatsService;
import jakarta.persistence.Column;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/seats")
@CrossOrigin(origins = "*")
public class SeatsController {

    @Autowired private SeatsService seatsService;

    @PostMapping(path = "/new/reservation")
    public ResponseEntity<?> createReservation(@RequestBody FlightDataToBuyDTO flightDataToBuyDTO) {
        try {
            String result = seatsService.buySeatsInThePlane(flightDataToBuyDTO);
           return ResponseEntity.status(HttpStatus.OK).body(result);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/user/{userId}")
    public List<SeatAndPlaneDTO> getAllSeatsByUserId(@PathVariable Long userId) {
        return seatsService.getAllSeatsByUserId(userId);
    }

    @GetMapping("/flight/{flightId}")
    public List<Seats> getAllSeatsByFlightId(@PathVariable Long flightId) {
        return seatsService.getAllSeatsByFlightId(flightId);
    }

    @GetMapping("/countDiscount/flight/{flightId}")
    public List<SeatsWithPriceDTO> getWithCalculatedPriceSeats(@PathVariable Long flightId) {
        return seatsService.countNewPriceAndPercentage(flightId);
    }

    @PostMapping("/cancel")
    public ResponseToConfirmDTO cancelBought(@RequestBody RequestBoughtDataDTO requestBoughtDataDTO) {
        try {
            return seatsService.cancelBought(requestBoughtDataDTO);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while processing your request");
        }
    }
    @PostMapping("/create/all")
    public ResponseEntity<String> createAll(@RequestBody List<SeatDTO> seatDTOList) {
        try {
            seatsService.createListOfSeats(seatDTOList);
            return new ResponseEntity<>("Seats created successfully", HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Error creating seats: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
