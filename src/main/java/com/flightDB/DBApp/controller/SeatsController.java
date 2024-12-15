package com.flightDB.DBApp.controller;

import com.flightDB.DBApp.dtos.SeatsWithPriceDTO;
import com.flightDB.DBApp.dtos.request.FlightDataToBuyDTO;
import com.flightDB.DBApp.dtos.request.RequestBoughtDataDTO;
import com.flightDB.DBApp.dtos.request.SeatDTO;
import com.flightDB.DBApp.dtos.response.ResponseToConfirmDTO;
import com.flightDB.DBApp.dtos.response.SeatAndPlaneDTO;
import com.flightDB.DBApp.model.Seats;
import com.flightDB.DBApp.service.SeatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@RestController
@RequestMapping("/api/seats")
@CrossOrigin(origins = "*")
public class SeatsController {

    @Autowired
    private SeatsService seatsService;

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

    @PostMapping("/delete/all")
    public ResponseEntity<String> deleteSeats(@RequestBody List<SeatDTO> seatDTOList) {
        try {
            seatsService.deleteListOfSeats(seatDTOList);
            return new ResponseEntity<>("Seats deleted successfully", HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting seats: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/flight/seats/{flightId}")
    public ResponseEntity<String> deleteAllSeatsFromFlight(@PathVariable("flightId") Long flightId) {
        try {
            seatsService.deleteAllSeatsFromFlight(flightId);
            return new ResponseEntity<>("Seats deleted successfully on flight " + flightId, HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>("Error deleting seats: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("update/all")
    public ResponseEntity<String> updateSeats(@RequestBody List<SeatDTO> seatDTOList) {
        try {
            seatsService.updateList(seatDTOList);
            return new ResponseEntity<>("Seats updated successfully", HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>("Error updating seas: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}