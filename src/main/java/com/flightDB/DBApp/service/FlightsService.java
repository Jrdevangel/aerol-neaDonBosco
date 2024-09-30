package com.flightDB.DBApp.service;

import com.flightDB.DBApp.model.Flight;
import com.flightDB.DBApp.repository.IFlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
        List<Flight> availableFlight = new ArrayList<>();

        for (Flight flight : flightList) {
            if (flight.getOrigin().getCountry().equals(originCountry) && flight.getDestination().getCountry().equals(destinationCountry)) {
                if (flight.getOrigin().getCity().equals(originCity) && flight.getDestination().getCity().equals(destinationCity)) {
                    if (localDate == null) {
                        availableFlight.add(flight);
                    } else {
                        if (flight.getDepartureTime() != null && flight.getDepartureTime().equals(localDate)) {
                            availableFlight.add(flight);
                        }
                    }
                }
            }
        }
        return availableFlight;
    }

    public Flight createFlight (Flight flight) {
        return iFlightRepository.save(flight);
    }


}
