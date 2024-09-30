package com.flightDB.DBApp.service;

import com.flightDB.DBApp.model.Passengers;
import com.flightDB.DBApp.repository.IPassengersRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PassengerServiceTest {

    @Mock
    private IPassengersRepository passengersRepository;

    @InjectMocks
    private PassengersService passengersService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test para crear un pasajero
    @Test
    public void testCreatePassenger() {
        Passengers passenger = new Passengers(1L, 100, 50);
        when(passengersRepository.save(any(Passengers.class))).thenReturn(passenger);

        Passengers createdPassenger = passengersService.createPassenger(passenger);

        assertNotNull(createdPassenger);
        assertEquals(100, createdPassenger.getCapacity());
        assertEquals(50, createdPassenger.getReservedSeats());
        verify(passengersRepository, times(1)).save(passenger);
    }

    // Test para obtener un pasajero por ID
    @Test
    public void testGetPassengerById() {
        Passengers passenger = new Passengers(1L, 100, 50);
        when(passengersRepository.findById(1L)).thenReturn(Optional.of(passenger));

        Optional<Passengers> foundPassenger = passengersService.getPassengerById(1L);

        assertTrue(foundPassenger.isPresent());
        assertEquals(1L, foundPassenger.get().getId());
        verify(passengersRepository, times(1)).findById(1L);
    }

    // Test para obtener todos los pasajeros
    @Test
    public void testGetAllPassengers() {
        Passengers passenger1 = new Passengers(1L, 100, 50);
        Passengers passenger2 = new Passengers(2L, 200, 150);
        List<Passengers> passengersList = new ArrayList<>(List.of(passenger1, passenger2));

        when(passengersRepository.findAll()).thenReturn(passengersList);

        List<Passengers> foundPassengers = passengersService.getAllPassengers();

        assertEquals(2, foundPassengers.size());
        verify(passengersRepository, times(1)).findAll();
    }

    // Test para actualizar un pasajero
    @Test
    public void testUpdatePassenger() {
        Passengers existingPassenger = new Passengers(1L, 100, 50);
        Passengers updatedPassenger = new Passengers(1L, 120, 60);

        when(passengersRepository.findById(1L)).thenReturn(Optional.of(existingPassenger));
        when(passengersRepository.save(any(Passengers.class))).thenReturn(updatedPassenger);

        Passengers result = passengersService.updatePassenger(1L, updatedPassenger);

        assertNotNull(result);
        assertEquals(120, result.getCapacity());
        assertEquals(60, result.getReservedSeats());
        verify(passengersRepository, times(1)).findById(1L);
        verify(passengersRepository, times(1)).save(existingPassenger);
    }

    // Test para eliminar un pasajero
    @Test
    public void testDeletePassenger() {
        doNothing().when(passengersRepository).deleteById(1L);

        passengersService.deletePassenger(1L);

        verify(passengersRepository, times(1)).deleteById(1L);
    }
}
