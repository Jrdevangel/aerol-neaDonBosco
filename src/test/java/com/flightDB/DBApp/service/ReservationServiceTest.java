package com.flightDB.DBApp.service;


import com.flightDB.DBApp.model.*;
import com.flightDB.DBApp.repository.IFlightRepository;
import com.flightDB.DBApp.repository.IPassengersRepository;
import com.flightDB.DBApp.repository.IReservationRepository;
import com.flightDB.DBApp.repository.IWalletRepository;
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

class ReservationServiceTest {

    @Mock
    private IReservationRepository reservationRepository;

    @Mock
    private IFlightRepository iFlightRepository;

    @Mock
    private IWalletRepository iWalletRepository;

    @Mock
    private IPassengersRepository iPassengersRepository;

    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateReservation_success() {
        User user = new User();
        user.setWallet(new Wallet());
        user.getWallet().setEuro(10);

        Flight flight = new Flight();
        Passengers passengers = new Passengers();
        passengers.setReservedSeats(150);
        passengers.setCapacity(200);
        flight.setPassengers(passengers);
        flight.setCostEuro(5);

        Reservation reservation = new Reservation();
        reservation.setReservedSeats(5);
        reservation.setFlight(flight);
        reservation.setUser(user);

        when(iPassengersRepository.save(any(Passengers.class))).thenReturn(passengers);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        when(iWalletRepository.save(any(Wallet.class))).thenReturn(user.getWallet());

        Reservation result = reservationService.createReservation(reservation);

        assertNotNull(result);
        assertEquals(155, passengers.getReservedSeats());
        verify(iPassengersRepository, times(1)).save(passengers);
        verify(reservationRepository, times(1)).save(reservation);
        verify(iWalletRepository, times(1)).save(user.getWallet());
        assertEquals(5, user.getWallet().getEuro());
    }

    @Test
    public void testCreateReservation_success_fullFlight() {
        User user = new User();
        user.setWallet(new Wallet());
        user.getWallet().setEuro(10);

        Flight flight = new Flight();
        Passengers passengers = new Passengers();
        passengers.setReservedSeats(190);
        passengers.setCapacity(200);
        flight.setPassengers(passengers);
        flight.setCostEuro(5);

        Reservation reservation = new Reservation();
        reservation.setReservedSeats(10);
        reservation.setFlight(flight);
        reservation.setUser(user);

        when(iPassengersRepository.save(any(Passengers.class))).thenReturn(passengers);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        when(iWalletRepository.save(any(Wallet.class))).thenReturn(user.getWallet());
        when(iFlightRepository.save(any(Flight.class))).thenReturn(flight);

        Reservation result = reservationService.createReservation(reservation);

        assertNotNull(result);
        assertEquals(200, passengers.getReservedSeats());
        assertTrue(flight.isAvailableSeat());
        verify(iPassengersRepository, times(1)).save(passengers);
        verify(reservationRepository, times(1)).save(reservation);
        verify(iFlightRepository, times(1)).save(flight);
        verify(iWalletRepository, times(1)).save(user.getWallet());
    }

    @Test
    public void testCreateReservation_failure_notEnoughMoney() {
        User user = new User();
        user.setWallet(new Wallet());
        user.getWallet().setEuro(1);

        Flight flight = new Flight();
        Passengers passengers = new Passengers();
        passengers.setReservedSeats(150);
        passengers.setCapacity(200);
        flight.setPassengers(passengers);
        flight.setCostEuro(5);

        Reservation reservation = new Reservation();
        reservation.setReservedSeats(5);
        reservation.setFlight(flight);
        reservation.setUser(user);

        when(iPassengersRepository.save(any(Passengers.class))).thenReturn(passengers);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.createReservation(reservation);
        });

        assertEquals("User don't have enough money", exception.getMessage());
        verify(iPassengersRepository, times(0)).save(any(Passengers.class));
        verify(reservationRepository, times(0)).save(any(Reservation.class));
    }

    @Test
    public void testCreateReservation_failure_noAvailableSeats() {
        User user = new User();
        user.setWallet(new Wallet());
        user.getWallet().setEuro(10);

        Flight flight = new Flight();
        Passengers passengers = new Passengers();
        passengers.setReservedSeats(200);
        passengers.setCapacity(200);
        flight.setPassengers(passengers);
        flight.setCostEuro(5);

        Reservation reservation = new Reservation();
        reservation.setReservedSeats(5);
        reservation.setFlight(flight);
        reservation.setUser(user);

        when(iPassengersRepository.save(any(Passengers.class))).thenReturn(passengers);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.createReservation(reservation);
        });

        assertEquals("There is no available seats", exception.getMessage());
        verify(iPassengersRepository, times(0)).save(any(Passengers.class));
        verify(reservationRepository, times(0)).save(any(Reservation.class));
    }





    @Test
    public void testGetReservationById() {
        Reservation reservation = new Reservation();  // Crea un objeto mock de reserva
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        Optional<Reservation> foundReservation = reservationService.getReservationById(1L);

        assertTrue(foundReservation.isPresent()); // Verifica que se encontró la reserva
        verify(reservationRepository, times(1)).findById(1L); // Verifica que se llamó al método findById con el ID correcto
    }

    // Test para obtener todas las reservas
    @Test
    public void testGetAllReservations() {
        Reservation reservation1 = new Reservation();  // Mockea dos objetos de reserva
        Reservation reservation2 = new Reservation();
        List<Reservation> reservationsList = new ArrayList<>(List.of(reservation1, reservation2));

        when(reservationRepository.findAll()).thenReturn(reservationsList);  // Simula la respuesta del repositorio

        List<Reservation> foundReservations = reservationService.getAllReservations();

        assertEquals(2, foundReservations.size());  // Verifica que se obtuvieron dos reservas
        verify(reservationRepository, times(1)).findAll();  // Verifica que el método findAll fue llamado una vez
    }

    // Test para actualizar una reserva
    @Test
    public void testUpdateReservation() {
        Reservation existingReservation = new Reservation();  // Crea una reserva existente
        Reservation updatedReservation = new Reservation();   // Crea una reserva actualizada

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(existingReservation));  // Simula la reserva existente
        when(reservationRepository.save(any(Reservation.class))).thenReturn(updatedReservation); // Simula la actualización de la reserva

        Reservation result = reservationService.updateReservation(1L, updatedReservation);

        assertNotNull(result);
        verify(reservationRepository, times(1)).findById(1L);  // Verifica que se buscó la reserva original
        verify(reservationRepository, times(1)).save(existingReservation);  // Verifica que se guardó la reserva actualizada
    }
    @Test
    public void testDeleteReservation() {
        doNothing().when(reservationRepository).deleteById(1L);  // Simula la operación de borrado

        reservationService.deleteReservation(1L);

        verify(reservationRepository, times(1)).deleteById(1L);  // Verifica que el método deleteById fue llamado una vez con el ID correcto
    }
}
