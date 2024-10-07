package com.flightDB.DBApp.service;

import com.flightDB.DBApp.model.Flight;
import com.flightDB.DBApp.model.Passengers;
import com.flightDB.DBApp.model.Reservation;
import com.flightDB.DBApp.model.User;
import com.flightDB.DBApp.repository.IReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ReservationService {

    private final FlightsService flightsService;
    private final UserService userService;
    private final WalletService walletService;
    private final PassengersService passengersService;
    private final IReservationRepository reservationRepository;

    @Autowired
    public ReservationService(FlightsService flightsService, UserService userService, WalletService walletService, PassengersService passengersService, IReservationRepository reservationRepository) {
        this.flightsService = flightsService;
        this.userService = userService;
        this.walletService = walletService;
        this.passengersService = passengersService;
        this.reservationRepository = reservationRepository;
    }

    public Reservation buyReservation(Reservation reservation) {
        Flight flight = flightsService.getFlightById(reservation.getFlight().getId());
        if(flight.getDepartureTime().isAfter(LocalDateTime.now())) {
            User user = userService.getUserById(reservation.getUser().getId());

            Passengers passengers = flight.getPassengers();

            validateSeatAvailability(reservation, passengers);
            double totalCost = calculateTotalCost(flight, reservation);
            validateUserBalance(user, totalCost);

            updateUserWallet(user, totalCost);
            updateFlightAndPassengers(flight, passengers, reservation);
            return finalizeReservation(reservation, user, flight);
        } else {
            throw new IllegalArgumentException("The time of flight has ended.");
        }
    }

    private void validateSeatAvailability(Reservation reservation, Passengers passengers) {
        int newReservedSeats = passengers.getReservedSeats() + reservation.getReservedSeats();
        if (newReservedSeats > passengers.getCapacity()) {
            throw new IllegalArgumentException("There are no available seats");
        }
    }

    private double calculateTotalCost(Flight flight, Reservation reservation) {
        return flight.getCostEuro() * reservation.getReservedSeats();
    }

    private void validateUserBalance(User user, double totalCost) {
        if (user.getWallet().getEuro() < totalCost) {
            throw new IllegalArgumentException("User doesn't have enough money");
        }
    }

    private void updateUserWallet(User user, double totalCost) {
        double newBalance = user.getWallet().getEuro() - totalCost;
        user.getWallet().setEuro(newBalance);
        walletService.saveWallet(user.getWallet());
    }

    private void updateFlightAndPassengers(Flight flight, Passengers passengers, Reservation reservation) {
        passengers.setReservedSeats(passengers.getReservedSeats() + reservation.getReservedSeats());
        passengersService.savePassengers(passengers);

        if (passengers.getReservedSeats() == passengers.getCapacity()) {
            flight.setAvailableSeat(false);
        }
        flightsService.saveFlight(flight);
    }

    private Reservation finalizeReservation(Reservation reservation, User user, Flight flight) {
        reservation.setUser(user);
        reservation.setFlight(flight);
        return reservationRepository.save(reservation);
    }

    public String returnReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new IllegalArgumentException("Reservation not found"));
        if(reservation.getFlight().getDepartureTime().isAfter(LocalDateTime.now())) {
            double userSumMoney = calculateRefundAmount(reservation);
            updateFlightAndUserWallet(reservation, userSumMoney);

            reservationRepository.deleteById(reservationId);
            return "Has been returned " + userSumMoney + " euro.";
        } else {
            return "You can't return because the time has ended";
        }
    }

    private double calculateRefundAmount(Reservation reservation) {
        return reservation.getUser().getWallet().getEuro() +
                (reservation.getFlight().getCostEuro() * reservation.getReservedSeats());
    }

    private void updateFlightAndUserWallet(Reservation reservation, double userSumMoney) {
        Flight flight = reservation.getFlight();
        Passengers passengers = flight.getPassengers();

        int newReservedSeats = passengers.getReservedSeats() - reservation.getReservedSeats();
        passengers.setReservedSeats(newReservedSeats);

        if (newReservedSeats <= passengers.getCapacity()) {
            flight.setAvailableSeat(true);
            flightsService.saveFlight(flight);
            passengersService.savePassengers(passengers);

            reservation.getUser().getWallet().setEuro(userSumMoney);
            walletService.saveWallet(reservation.getUser().getWallet());
        } else {
            throw new IllegalArgumentException("You can't return because the time has ended");
        }
    }

    public Reservation updateReservation(Long id, Reservation reservation) {
        return reservationRepository.findById(id)
                .map(existingReservation -> {
                    existingReservation.setFlight(reservation.getFlight());
                    existingReservation.setUser(reservation.getUser());
                    return reservationRepository.save(existingReservation);
                })
                .orElse(null);
    }

    public Optional<Reservation> getReservationById(Long id) {
        return reservationRepository.findById(id);
    }

    public List<Reservation> getAllReservations() {
        return StreamSupport.stream(reservationRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    public List<Reservation> getAllReservationByUserId(Long userId) {
        return reservationRepository.findByUserId(userId);
    }
}
