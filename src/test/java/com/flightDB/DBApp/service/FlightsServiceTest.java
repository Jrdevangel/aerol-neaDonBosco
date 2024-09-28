package com.flightDB.DBApp.service;

import com.flightDB.DBApp.model.Flight;
import com.flightDB.DBApp.model.Passengers;
import com.flightDB.DBApp.model.Routes;
import com.flightDB.DBApp.repository.IFlightRepository;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class FlightsServiceTest {

    private MockMvc mockMvc;

    @Mock
    IFlightRepository iFlightRepository;

    @InjectMocks
    FlightsService flightsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllFlight() {
        Routes origin = new Routes(1L, "Spain", "Madrid");
        Routes destination = new Routes(2L, "France", "Paris");
        Passengers passengers = new Passengers(1L, 200, 100);
        Flight flight = new Flight(1L, null, destination, origin, passengers);
        ArrayList<Flight> flightList = new ArrayList<>();
        flightList.add(flight);
        when(iFlightRepository.findAll()).thenReturn(flightList);
        List<Flight> result = flightsService.getAllFlight();
        assertEquals(flight, result.get(0));
        assertEquals(1, result.size());

    }

    @Test
    void deleteFlight() {
        Long flightId = 1L;
        doNothing().when(iFlightRepository).deleteById(flightId);

        flightsService.deleteFright(flightId);

        verify(iFlightRepository, times(1)).deleteById(flightId);
    }


    @Test
    void updateFlight() {
        Routes origin = new Routes(1L, "Spain", "Madrid");
        Routes destination = new Routes(2L, "France", "Paris");
        Passengers passengers = new Passengers(1L, 200, 100);
        Flight existingFlight = new Flight(1L, null, destination, origin, passengers);
        Flight newBoeing = new Flight(1L, LocalDate.now(), destination, origin, passengers);

        when(iFlightRepository.findById(1L)).thenReturn(Optional.of(existingFlight));
        when(iFlightRepository.save(newBoeing)).thenReturn(newBoeing);

        Flight result = flightsService.updateFlight(1L, newBoeing);

        assertNotNull(result);
        assertEquals(newBoeing.getDepartureTime(), result.getDepartureTime());
        assertEquals(newBoeing.getDestination(), result.getDestination());
        assertEquals(newBoeing.getOrigin(), result.getOrigin());
        assertEquals(newBoeing.getPassengers(), result.getPassengers());
    }

    @Test
    void getAllFlightBySearchWithoutDate() {
        Routes origin = new Routes(1L, "Spain", "Madrid");
        Routes destination = new Routes(2L, "France", "Paris");
        Passengers passengers = new Passengers(1L, 200, 100);

        List<Flight> flightList = new ArrayList<>();
        Flight rainer = new Flight(1L, null, destination, origin, passengers);
        Flight boeing = new Flight(2L, LocalDate.now(), destination, origin, passengers);

        flightList.add(rainer);
        flightList.add(boeing);

        when(iFlightRepository.findAll()).thenReturn(flightList);

        List<Flight> result = flightsService.getAllFlightBySearch("Spain", "Madrid", "France", "Paris", null);

        assertNotNull(result);
        assertEquals(2, result.size());
    }

    @Test
    void getAllFlightBySearchWithDate() {
        Routes origin = new Routes(1L, "Spain", "Madrid");
        Routes destination = new Routes(2L, "France", "Paris");
        Passengers passengers = new Passengers(1L, 200, 100);

        List<Flight> flightList = new ArrayList<>();
        Flight rainer = new Flight(1L, null, destination, origin, passengers);
        Flight boeing = new Flight(2L, LocalDate.now(), destination, origin, passengers);

        flightList.add(rainer);
        flightList.add(boeing);

        when(iFlightRepository.findAll()).thenReturn(flightList);
        LocalDate testDate = LocalDate.now();
        List<Flight> result = flightsService.getAllFlightBySearch("Spain", "Madrid", "France", "Paris", testDate);

        assertNotNull(result);
        assertEquals(1, result.size());

    }

    @Test
    void createFlight() {
        Routes origin = new Routes(1L, "Spain", "Madrid");
        Routes destination = new Routes(2L, "France", "Paris");
        Passengers passengers = new Passengers(1L, 200, 100);
        Flight flight = new Flight(1L, null, destination, origin, passengers);
        when(iFlightRepository.save(flight)).thenReturn(flight);

        Flight result = flightsService.createFlight(flight);

        assertEquals(flight, result);
        verify(iFlightRepository).save(flight);
    }
}