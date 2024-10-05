package com.flightDB.DBApp.service;

import com.flightDB.DBApp.model.*;
import com.flightDB.DBApp.repository.*;
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

    @Mock
    private IUserRepository iUserRepository;

    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testBuyReservation_success() {
        User user = new User();
        user.setId(1L);
        Wallet wallet = new Wallet();
        wallet.setEuro(25);
        user.setWallet(wallet);

        Flight flight = new Flight();
        flight.setId(1L);
        Passengers passengers = new Passengers();
        passengers.setReservedSeats(150);
        passengers.setCapacity(200);
        flight.setPassengers(passengers);
        flight.setCostEuro(5);

        Reservation reservation = new Reservation();
        reservation.setReservedSeats(5);
        reservation.setFlight(flight);
        reservation.setUser(user);

        when(iUserRepository.findById(1L)).thenReturn(Optional.of(user));
        when(iFlightRepository.findById(1L)).thenReturn(Optional.of(flight));
        when(iPassengersRepository.save(any(Passengers.class))).thenReturn(passengers);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        when(iWalletRepository.save(any(Wallet.class))).thenReturn(user.getWallet());

        Reservation result = reservationService.buyReservation(reservation);

        assertNotNull(result);
        assertEquals(155, passengers.getReservedSeats());
        assertEquals(5, reservation.getReservedSeats());
        verify(iPassengersRepository, times(1)).save(passengers);
        verify(reservationRepository, times(1)).save(reservation);
        verify(iWalletRepository, times(1)).save(user.getWallet());
        assertEquals(0, user.getWallet().getEuro());
    }

    @Test
    public void testBuyReservation_success_fullFlight() {
        User user = new User();
        user.setId(1L);
        Wallet wallet = new Wallet();
        wallet.setEuro(50);
        user.setWallet(wallet);

        Flight flight = new Flight();
        flight.setId(1L);
        Passengers passengers = new Passengers();
        passengers.setReservedSeats(190);
        passengers.setCapacity(200);
        flight.setPassengers(passengers);
        flight.setCostEuro(5);

        Reservation reservation = new Reservation();
        reservation.setReservedSeats(10);
        reservation.setFlight(flight);
        reservation.setUser(user);

        when(iUserRepository.findById(1L)).thenReturn(Optional.of(user));
        when(iFlightRepository.findById(1L)).thenReturn(Optional.of(flight));
        when(iPassengersRepository.save(any(Passengers.class))).thenReturn(passengers);
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);
        when(iWalletRepository.save(any(Wallet.class))).thenReturn(user.getWallet());
        when(iFlightRepository.save(any(Flight.class))).thenReturn(flight);

        Reservation result = reservationService.buyReservation(reservation);

        assertNotNull(result);
        assertEquals(200, passengers.getReservedSeats());
        assertFalse(flight.isAvailableSeat());
        verify(iPassengersRepository, times(1)).save(passengers);
        verify(reservationRepository, times(1)).save(reservation);
        verify(iFlightRepository, times(1)).save(flight);
        verify(iWalletRepository, times(1)).save(user.getWallet());
    }

    @Test
    public void testBuyReservation_failure_notEnoughMoney() {
        User user = new User();
        user.setId(1L);
        Wallet wallet = new Wallet();
        wallet.setEuro(1);
        user.setWallet(wallet);

        Flight flight = new Flight();
        flight.setId(1L);
        Passengers passengers = new Passengers();
        passengers.setReservedSeats(150);
        passengers.setCapacity(200);
        flight.setPassengers(passengers);
        flight.setCostEuro(5);

        Reservation reservation = new Reservation();
        reservation.setReservedSeats(5);
        reservation.setFlight(flight);
        reservation.setUser(user);

        when(iUserRepository.findById(1L)).thenReturn(Optional.of(user));
        when(iFlightRepository.findById(1L)).thenReturn(Optional.of(flight));
        when(iPassengersRepository.save(any(Passengers.class))).thenReturn(passengers);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.buyReservation(reservation);
        });

        assertEquals("User doesn't have enough money", exception.getMessage());
        verify(iPassengersRepository, times(0)).save(any(Passengers.class));
        verify(reservationRepository, times(0)).save(any(Reservation.class));
    }

    @Test
    public void testBuyReservation_failure_noAvailableSeats() {
        User user = new User();
        user.setId(1L);
        Wallet wallet = new Wallet();
        wallet.setEuro(10);
        user.setWallet(wallet);

        Flight flight = new Flight();
        flight.setId(1L);
        Passengers passengers = new Passengers();
        passengers.setReservedSeats(200);
        passengers.setCapacity(200);
        flight.setPassengers(passengers);
        flight.setCostEuro(5);

        Reservation reservation = new Reservation();
        reservation.setReservedSeats(5);
        reservation.setFlight(flight);
        reservation.setUser(user);

        when(iUserRepository.findById(1L)).thenReturn(Optional.of(user));
        when(iFlightRepository.findById(1L)).thenReturn(Optional.of(flight));
        when(iPassengersRepository.save(any(Passengers.class))).thenReturn(passengers);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            reservationService.buyReservation(reservation);
        });

        assertEquals("There are no available seats", exception.getMessage());
        verify(iPassengersRepository, times(0)).save(any(Passengers.class));
        verify(reservationRepository, times(0)).save(any(Reservation.class));
    }

    @Test
    public void testGetReservationById() {
        Reservation reservation = new Reservation();
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        Optional<Reservation> foundReservation = reservationService.getReservationById(1L);

        assertTrue(foundReservation.isPresent());
        verify(reservationRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetAllReservations() {
        Reservation reservation1 = new Reservation();
        Reservation reservation2 = new Reservation();
        List<Reservation> reservationsList = new ArrayList<>(List.of(reservation1, reservation2));

        when(reservationRepository.findAll()).thenReturn(reservationsList);

        List<Reservation> foundReservations = reservationService.getAllReservations();

        assertEquals(2, foundReservations.size());
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    public void testUpdateReservation() {
        Reservation existingReservation = new Reservation();
        existingReservation.setId(1L);
        Reservation updatedReservation = new Reservation();

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(existingReservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(updatedReservation);

        Reservation result = reservationService.updateReservation(1L, updatedReservation);

        assertNotNull(result);
        verify(reservationRepository, times(1)).findById(1L);
        verify(reservationRepository, times(1)).save(existingReservation);
    }

    @Test
    public void testDeleteReservation() {
        doNothing().when(reservationRepository).deleteById(1L);

        reservationService.deleteReservation(1L);

        verify(reservationRepository, times(1)).deleteById(1L);
    }

    @Test
    void returnReservation() {
        User user = new User();
        user.setId(1L);
        user.setWallet(new Wallet());
        user.getWallet().setEuro(0);

        Flight flight = new Flight();
        flight.setId(1L);
        Passengers passengers = new Passengers();
        passengers.setReservedSeats(190);
        passengers.setCapacity(200);
        flight.setPassengers(passengers);
        flight.setCostEuro(5);

        Reservation reservation = new Reservation();
        reservation.setReservedSeats(10);
        reservation.setFlight(flight);
        reservation.setUser(user);
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));
        when(iWalletRepository.save(user.getWallet())).thenReturn(user.getWallet());
        when(iFlightRepository.save(flight)).thenReturn(flight);
        when(iPassengersRepository.save(flight.getPassengers())).thenReturn(flight.getPassengers());
        when(reservationRepository.save(reservation)).thenReturn(reservation);

        String response = reservationService.returnReservation(1L);
        assertEquals("Has been returned 50.0 euro.", response);
        assertEquals(180, passengers.getReservedSeats());
    }

}
