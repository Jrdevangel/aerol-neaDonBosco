
package com.flightDB.DBApp.controller;

import com.flightDB.DBApp.model.Passengers;
import com.flightDB.DBApp.service.PassengersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class PassengersController {

    private final PassengersService passengersService;

    @Autowired
    public PassengersController(PassengersService passengersService) {
        this.passengersService = passengersService;
    }


    @PostMapping(path = "/new/passengers")
    public ResponseEntity<Passengers> createPassenger(@RequestBody Passengers passenger) {
        Passengers created = passengersService.createPassenger(passenger);
        return ResponseEntity.ok(created);
    }


    @GetMapping(path = "/passengers/{id}")
    public ResponseEntity<Passengers> getPassengerById(@PathVariable Long id) {
        Optional<Passengers> passengerOpt = passengersService.getPassengerById(id);
        if (passengerOpt.isPresent()) {
            return ResponseEntity.ok(passengerOpt.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path = "/passengers")
    public ResponseEntity<List<Passengers>> getAllPassengers() {
        List<Passengers> passengers = passengersService.getAllPassengers();
        return ResponseEntity.ok(passengers);
    }

    @PutMapping(path = "/update/passengers/{id}")
    public ResponseEntity<Passengers> updatePassenger(@PathVariable Long id, @RequestBody Passengers passenger) {
        Passengers updated = passengersService.updatePassenger(id, passenger);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(path = "/delete/passengers/{id}")
    public ResponseEntity<Void> deletePassenger(@PathVariable Long id) {
        passengersService.deletePassenger(id);
        return ResponseEntity.noContent().build();
    }
}