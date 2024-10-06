package com.flightDB.DBApp.controller;

import com.flightDB.DBApp.model.Reservation;
import com.flightDB.DBApp.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class ReservationController {

    private final ReservationService reservationService;

    @Autowired
    public ReservationController(ReservationService reservationService){
        this.reservationService = reservationService;
    }


    @PostMapping(path = "/new/reservation")
    public ResponseEntity<Reservation> createReservation(@RequestBody Reservation reservation) {
        try {
            Reservation created = reservationService.buyReservation(reservation);
            return ResponseEntity.ok(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }



    @GetMapping(path = "/reservation/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id){
        Optional<Reservation> reservationOpt = reservationService.getReservationById(id);
        return reservationOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }


    @GetMapping(path = "/reservation")
    public ResponseEntity<List<Reservation>> getAllReservations(){
        List<Reservation> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }


    @PutMapping(path = "/update/reservation/{id}")
    public ResponseEntity<?> buyReservation(@RequestBody Reservation reservation) {
        try {
            Reservation savedReservation = reservationService.buyReservation(reservation);
            return new ResponseEntity<>(savedReservation, HttpStatus.OK);
        } catch (IllegalArgumentException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @DeleteMapping(path = "/delete/reservation/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id){
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/return/{reservationId}")
    public String returnReservation(@PathVariable Long reservationId) {
        return reservationService.returnReservation(reservationId);
    }

    @GetMapping(path = "/reservation/user/{userId}")
    public List<Reservation> getAllReservationByUserId(@PathVariable Long userId) {
        return reservationService.getAllReservationByUserId(userId);
    }
}
