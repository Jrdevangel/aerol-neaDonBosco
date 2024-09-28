
package com.flightDB.DBApp.controller;

import com.flightDB.DBApp.model.Reservation;
import com.flightDB.DBApp.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
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


    @PostMapping(path = "/reservations")
    public ResponseEntity<Reservation> createReservation(@RequestBody Reservation reservation){
        Reservation created = reservationService.createReservation(reservation);
        return ResponseEntity.ok(created);
    }


    @GetMapping(path = "/reservations/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id){
        Optional<Reservation> reservationOpt = reservationService.getReservationById(id);
        if (reservationOpt.isPresent()) {
            return ResponseEntity.ok(reservationOpt.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping(path = "/reservations")
    public ResponseEntity<List<Reservation>> getAllReservations(){
        List<Reservation> reservations = reservationService.getAllReservations();
        return ResponseEntity.ok(reservations);
    }


    @PutMapping(path = "/reservations/{id}")
    public ResponseEntity<Reservation> updateReservation(@PathVariable Long id, @RequestBody Reservation reservation){
        Reservation updated = reservationService.updateReservation(id, reservation);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping(path = "/reservations/{id}")
    public ResponseEntity<Void> deleteReservation(@PathVariable Long id){
        reservationService.deleteReservation(id);
        return ResponseEntity.noContent().build();
    }
}
