package com.flightDB.DBApp.service;

import com.flightDB.DBApp.dtos.request.FlightSearchDataDTO;
import com.flightDB.DBApp.model.Flight;
import com.flightDB.DBApp.model.Routes;
import com.flightDB.DBApp.repository.IFlightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FlightsService {

    @Autowired private IFlightRepository iFlightRepository;

    @Autowired private RoutesService routesService;

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



    public List<Flight> getAllFlightBySearch(FlightSearchDataDTO flightSearchDataDTO) {
        Routes originRoute = routesService.getRouteByCountryAndCity(flightSearchDataDTO.getOriginCountry(), flightSearchDataDTO.getOriginCity());
        Routes destinationRoute = routesService.getRouteByCountryAndCity(flightSearchDataDTO.getDestinationCountry(), flightSearchDataDTO.getDestinationCity());
        List<Flight> flightList = iFlightRepository.findByOriginIdAndDestinationId(originRoute.getId(), destinationRoute.getId());
        List<Flight> filteredFlights = new ArrayList<>();
        LocalDate startDate = flightSearchDataDTO.getStartLocalDate();
        LocalDate finishDate = flightSearchDataDTO.getFinishLocalDate();
        for (Flight flight : flightList) {
            LocalDateTime departureTime = flight.getDepartureTime();
            boolean matchesStartDate = (startDate == null || !departureTime.isBefore(startDate.atStartOfDay()));
            boolean matchesFinishDate = (finishDate == null || !departureTime.isAfter(finishDate.atStartOfDay()));
            if (matchesStartDate && matchesFinishDate) {
                filteredFlights.add(flight);
            }
        }
        return filteredFlights;
    }



    public Flight getFlightById(Long id) {
        return iFlightRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Flight not found"));
    }


    public Flight createFlight(Flight flight) {
        return iFlightRepository.save(flight);
    }


    public void saveFlight(Flight flight) {
        iFlightRepository.save(flight);
    }


}
