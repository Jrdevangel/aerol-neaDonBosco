package com.flightDB.DBApp.controller;

import com.flightDB.DBApp.model.Flight;
import com.flightDB.DBApp.service.FlightsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/flight")
@CrossOrigin
public class FlightsController {

    @Autowired
    FlightsService flightsService;

    @GetMapping(path = "/get")
    public List<Flight> getAllFlights() {
        return flightsService.getAllFlight();
    }
    @GetMapping(path = "/search")
    public List<Flight> getAllFlightBySearch(@RequestParam String originCountry, @RequestParam String originCity, @RequestParam String destinationCountry, @RequestParam String destinationCity, @RequestParam(required = false) LocalDate localDate) {
        return flightsService.getAllFlightBySearch(originCountry, originCity, destinationCountry, destinationCity, localDate);
    }
    @DeleteMapping(path = "/delete/{id}")
    public void deleteFlight(@PathVariable Long id) {
        flightsService.deleteFright(id);
    }
    @PutMapping(path = "/update/{id}")
    public Flight updateFlight(@PathVariable Long id, @RequestBody Flight newFlight) {
        return flightsService.updateFlight(id, newFlight);
    }
    @PostMapping(path = "/create")
    public Flight createFlight(@RequestBody Flight flight) {
        return flightsService.createFlight(flight);
    }
}
