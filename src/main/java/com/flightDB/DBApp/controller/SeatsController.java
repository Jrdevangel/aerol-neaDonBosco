package com.flightDB.DBApp.controller;

import com.flightDB.DBApp.model.Seats;
import com.flightDB.DBApp.service.SeatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class SeatsController {

//    private final SeatsService seatsService;
//
//    @Autowired
//    public SeatsController(SeatsService seatsService){
//        this.seatsService = seatsService;
//    }
//
//
//    @PostMapping(path = "/new/reservation")
//    public ResponseEntity<?> createReservation(@RequestBody Seats seats) {
//        try {
//            Seats created = seatsService.buyReservation(seats);
//            return ResponseEntity.ok(created);
//        } catch (IllegalArgumentException e) {
//            Map<String, String> errorResponse = new HashMap<>();
//            errorResponse.put("error", "Bad Request");
//            errorResponse.put("message", e.getMessage());
//            return ResponseEntity.badRequest().body(errorResponse);
//        } catch (Exception e) {
//            Map<String, String> errorResponse = new HashMap<>();
//            errorResponse.put("error", "Internal Server Error");
//            errorResponse.put("message", "An unexpected error occurred while processing your request");
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
//        }
//    }
//
//
//
//    @GetMapping(path = "/reservation/{id}")
//    public ResponseEntity<Seats> getReservationById(@PathVariable Long id){
//        Optional<Seats> reservationOpt = seatsService.getReservationById(id);
//        return reservationOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
//    }
//
//
//    @GetMapping(path = "/reservation")
//    public ResponseEntity<List<Seats>> getAllReservations(){
//        List<Seats> seats = seatsService.getAllReservations();
//        return ResponseEntity.ok(seats);
//    }
//
//
//    @PutMapping(path = "/update/reservation/{id}")
//    public ResponseEntity<?> updateReservation(@PathVariable Long id,@RequestBody Seats seats) {
//        try {
//            Seats savedSeats = seatsService.updateReservation(id , seats);
//            return new ResponseEntity<>(savedSeats, HttpStatus.OK);
//        } catch (IllegalArgumentException ex) {
//            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
//        }
//    }
//
//
//    @DeleteMapping(path = "/delete/reservation/{id}")
//    public ResponseEntity<Void> deleteReservation(@PathVariable Long id){
//        seatsService.deleteReservation(id);
//        return ResponseEntity.noContent().build();
//    }
//
//    @PutMapping(path = "/return/{reservationId}")
//    public String returnReservation(@PathVariable Long reservationId) {
//        return seatsService.returnReservation(reservationId);
//    }
//
//    @GetMapping(path = "/reservation/user/{userId}")
//    public List<Seats> getAllReservationByUserId(@PathVariable Long userId) {
//        return seatsService.getAllReservationByUserId(userId);
//    }
}
