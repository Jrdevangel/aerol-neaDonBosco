package com.flightDB.DBApp.controller;

import com.flightDB.DBApp.model.Passengers;
import com.flightDB.DBApp.service.PassengersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PassengerControllerTest {

    @Mock
    private PassengersService passengersService;

    @InjectMocks
    private PassengersController passengerController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testCreatePassenger() {
        Passengers passenger = new Passengers(1L, 100, 50);
        when(passengersService.createPassenger(any(Passengers.class))).thenReturn(passenger);

        ResponseEntity<Passengers> response = passengerController.createPassenger(passenger);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(100, response.getBody().getCapacity());
        assertEquals(50, response.getBody().getReservedSeats());
        verify(passengersService, times(1)).createPassenger(passenger);
    }

    @Test
    public void testGetPassengerById() {
        Passengers passenger = new Passengers(1L, 100, 50);
        when(passengersService.getPassengerById(1L)).thenReturn(Optional.of(passenger));

        ResponseEntity<Passengers> response = passengerController.getPassengerById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        verify(passengersService, times(1)).getPassengerById(1L);
    }

    @Test
    public void testGetAllPassengers() {
        Passengers passenger1 = new Passengers(1L, 100, 50);
        Passengers passenger2 = new Passengers(2L, 200, 150);
        List<Passengers> passengersList = new ArrayList<>(List.of(passenger1, passenger2));

        when(passengersService.getAllPassengers()).thenReturn(passengersList);

        ResponseEntity<List<Passengers>> response = passengerController.getAllPassengers();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(passengersService, times(1)).getAllPassengers();
    }

    @Test
    public void testUpdatePassenger() {
        Passengers existingPassenger = new Passengers(1L, 100, 50);
        Passengers updatedPassenger = new Passengers(1L, 120, 60);

        when(passengersService.updatePassenger(eq(1L), any(Passengers.class))).thenReturn(updatedPassenger);

        ResponseEntity<Passengers> response = passengerController.updatePassenger(1L, updatedPassenger);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(120, response.getBody().getCapacity());
        assertEquals(60, response.getBody().getReservedSeats());
        verify(passengersService, times(1)).updatePassenger(eq(1L), any(Passengers.class));
    }

    @Test
    public void testDeletePassenger() {
        doNothing().when(passengersService).deletePassenger(1L);

        ResponseEntity<Void> response = passengerController.deletePassenger(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(passengersService, times(1)).deletePassenger(1L);
    }
}
