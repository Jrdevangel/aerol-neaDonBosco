package com.flightDB.DBApp.service;

import com.flightDB.DBApp.model.Flight;
import com.flightDB.DBApp.repository.IFlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlightsService {

    @Autowired
    IFlightRepository iFlightRepository;


    public List<Flight> getAllFlight() {
        return (List<Flight>) iFlightRepository.findAll();
    }

    public void deleteFright(Long id) {
        iFlightRepository.deleteById(id);
    }

    public Flight updateFlight(Long id, Flight newFlight) {
        newFlight.setId(id);
        return iFlightRepository.save(newFlight);
    }

    public List<Flight> getAllFlightBySearch(String originCountry, String originCity, String destinationCountry, String destinationCity, LocalDate localDate) {
        List<Flight> flightList = (List<Flight>) iFlightRepository.findAll();

        return flightList.stream()
                .filter(flight -> flight.getOrigin().getCountry().equals(originCountry))
                .filter(flight -> flight.getDestination().getCountry().equals(destinationCountry))
                .filter(flight -> flight.getOrigin().getCity().equals(originCity))
                .filter(flight -> flight.getDestination().getCity().equals(destinationCity))
                .filter(flight -> localDate == null || (flight.getDepartureTime() != null
                        && flight.getDepartureTime().toLocalDate().equals(localDate)))
                .collect(Collectors.toList());
    }


    public Flight getFlightById(Long id) {
        return iFlightRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Flight not found"));
    }


    public Flight createFlight (Flight flight) {
        return iFlightRepository.save(flight);
    }

    public void saveFlight(Flight flight) {
        iFlightRepository.save(flight);
    }


}
