package com.flightDB.DBApp.service;

import com.flightDB.DBApp.model.Flight;
import com.flightDB.DBApp.model.Passengers;
import com.flightDB.DBApp.model.Reservation;
import com.flightDB.DBApp.model.User;
import com.flightDB.DBApp.model.Wallet;
import com.flightDB.DBApp.repository.IReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

    @InjectMocks
    private ReservationService reservationService;

    @Mock
    private FlightsService flightsService;

    @Mock
    private UserService userService;

    @Mock
    private WalletService walletService;

    @Mock
    private PassengersService passengersService;

    @Mock
    private IReservationRepository reservationRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void buyReservation_success() {
        User user = new User();
        user.setId(1L);
        Wallet wallet = new Wallet();
        wallet.setEuro(100.0);
        user.setWallet(wallet);

        Flight flight = new Flight();
        flight.setId(1L);
        flight.setCostEuro(20.0);
        Passengers passengers = new Passengers();
        passengers.setCapacity(10);
        passengers.setReservedSeats(0);
        flight.setPassengers(passengers);

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setFlight(flight);
        reservation.setReservedSeats(2);

        when(userService.getUserById(1L)).thenReturn(user);
        when(flightsService.getFlightById(1L)).thenReturn(flight);

        doNothing().when(walletService).saveWallet(any(Wallet.class));
        doNothing().when(passengersService).savePassengers(any(Passengers.class));
        doNothing().when(flightsService).saveFlight(any(Flight.class));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        Reservation savedReservation = reservationService.buyReservation(reservation);

        assertNotNull(savedReservation);
        assertEquals(reservation, savedReservation);
        assertEquals(60.0, user.getWallet().getEuro());
        assertEquals(2, passengers.getReservedSeats());
    }

    @Test
    void returnReservation_success() {
        User user = new User();
        user.setWallet(new Wallet());
        user.getWallet().setEuro(50.0);

        Flight flight = new Flight();
        flight.setCostEuro(20.0);
        Passengers passengers = new Passengers();
        passengers.setReservedSeats(2);
        passengers.setCapacity(10);
        flight.setPassengers(passengers);

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setFlight(flight);
        reservation.setReservedSeats(2);

        when(reservationRepository.findById(anyLong())).thenReturn(Optional.of(reservation));
        doNothing().when(walletService).saveWallet(any(Wallet.class));
        doNothing().when(passengersService).savePassengers(any(Passengers.class));
        doNothing().when(flightsService).saveFlight(any(Flight.class));

        String result = reservationService.returnReservation(1L);

        assertEquals("Has been returned 90.0 euro.", result);
        assertEquals(90.0, user.getWallet().getEuro());
    }

    @Test
    void updateReservation_success() {
        Reservation existingReservation = new Reservation();
        existingReservation.setId(1L);
        existingReservation.setUser(new User());
        existingReservation.setFlight(new Flight());

        Reservation newReservation = new Reservation();
        newReservation.setFlight(new Flight());
        newReservation.setUser(new User());

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(existingReservation));
        when(reservationRepository.save(any(Reservation.class))).thenReturn(existingReservation);

        Reservation updatedReservation = reservationService.updateReservation(1L, newReservation);

        assertNotNull(updatedReservation);
        assertEquals(existingReservation, updatedReservation);
    }

    @Test
    void updateReservation_notFound() {
        Reservation newReservation = new Reservation();
        newReservation.setFlight(new Flight());
        newReservation.setUser(new User());

        when(reservationRepository.findById(1L)).thenReturn(Optional.empty());

        Reservation updatedReservation = reservationService.updateReservation(1L, newReservation);

        assertNull(updatedReservation);
    }

    @Test
    void getReservationById_success() {
        Reservation reservation = new Reservation();
        reservation.setId(1L);

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        Optional<Reservation> foundReservation = reservationService.getReservationById(1L);

        assertTrue(foundReservation.isPresent());
        assertEquals(reservation, foundReservation.get());
    }

    @Test
    void getAllReservations_success() {
        Reservation reservation1 = new Reservation();
        Reservation reservation2 = new Reservation();
        List<Reservation> reservations = List.of(reservation1, reservation2);

        when(reservationRepository.findAll()).thenReturn(reservations);

        List<Reservation> foundReservations = reservationService.getAllReservations();

        assertEquals(2, foundReservations.size());
    }

    @Test
    void deleteReservation_success() {
        doNothing().when(reservationRepository).deleteById(anyLong());

        assertDoesNotThrow(() -> reservationService.deleteReservation(1L));
        verify(reservationRepository, times(1)).deleteById(1L);
    }

    @Test
    void getAllReservationByUserId_success() {
        User user = new User();
        user.setId(1L);
        Reservation reservation = new Reservation();
        reservation.setUser(user);
        List<Reservation> reservations = List.of(reservation);

        when(reservationRepository.findByUserId(1L)).thenReturn(reservations);

        List<Reservation> foundReservations = reservationService.getAllReservationByUserId(1L);

        assertEquals(1, foundReservations.size());
        assertEquals(reservation, foundReservations.get(0));
    }
}
