package com.flightDB.DBApp.controller;

import com.flightDB.DBApp.dtos.request.FlightSearchDataDTO;
import com.flightDB.DBApp.dtos.response.SaleFlightDTO;
import com.flightDB.DBApp.model.Flight;
import com.flightDB.DBApp.service.FlightsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping(path = "/get/{id}")
    public Flight getById(@PathVariable Long id) {
        return flightsService.getFlightById(id);
    }

    @GetMapping(path = "/search")
    public List<Flight> getAllFlightBySearch(@RequestBody FlightSearchDataDTO flightSearchDataDTO) {
        return flightsService.getAllFlightBySearch(flightSearchDataDTO);
    }
//    @GetMapping(path = "/sales")
//    public List<SaleFlightDTO> getAllSales() {
//        return flightsService.getAllFlightWhatIsOnSale();
//    }

    @DeleteMapping(path = "/delete/{id}")
    public void deleteFlight(@PathVariable Long id) {
        flightsService.deleteFright(id);
    }


    @PutMapping(path = "/update/{id}")
    public Flight updateFlight(@PathVariable Long id, @RequestBody Flight newFlight) {
        return flightsService.updateFlight(id, newFlight);
    }
    @PostMapping("/create")
    public ResponseEntity<Flight> createFlight(@RequestBody Flight flight) {
        Flight createdFlight = flightsService.createFlight(flight);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdFlight);
    }
}

