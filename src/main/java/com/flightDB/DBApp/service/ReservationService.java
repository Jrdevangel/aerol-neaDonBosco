// src/main/java/com/flightDB/DBApp/service/ReservationService.java

package com.flightDB.DBApp.service;

import com.flightDB.DBApp.model.Flight;
import com.flightDB.DBApp.model.Passengers;
import com.flightDB.DBApp.model.Reservation;
import com.flightDB.DBApp.model.User;
import com.flightDB.DBApp.repository.*;
import com.flightDB.DBApp.repository.IReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ReservationService {

    private final IReservationRepository reservationRepository;
    private final IPassengersRepository iPassengersRepository;
    private final IFlightRepository iFlightRepository;
    private final IWalletRepository iWalletRepository;
    private final IUserRepository iUserRepository;

    @Autowired
    public ReservationService(IReservationRepository reservationRepository, IPassengersRepository iPassengersRepository, IFlightRepository iFlightRepository, IWalletRepository iWalletRepository, IUserRepository iUserRepository) {
        this.reservationRepository = reservationRepository;
        this.iPassengersRepository = iPassengersRepository;
        this.iFlightRepository = iFlightRepository;
        this.iWalletRepository = iWalletRepository;
        this.iUserRepository = iUserRepository;
    }


    public Reservation buyReservation(Reservation reservation) {
        User user = iUserRepository.findById(reservation.getUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Flight flight = iFlightRepository.findById(reservation.getFlight().getId())
                .orElseThrow(() -> new IllegalArgumentException("Flight not found"));

        Passengers responsePassengers = flight.getPassengers();

        if (responsePassengers.getReservedSeats() + reservation.getReservedSeats() <= responsePassengers.getCapacity()) {
            if (user.getWallet().getEuro() >= flight.getCostEuro() * reservation.getReservedSeats()) {
                double lessMoney = user.getWallet().getEuro() - (flight.getCostEuro() * reservation.getReservedSeats());
                user.getWallet().setEuro(lessMoney);
                iWalletRepository.save(user.getWallet());

                if (responsePassengers.getReservedSeats() + reservation.getReservedSeats() == responsePassengers.getCapacity()) {
                    flight.setAvailableSeat(false);
                    iFlightRepository.save(flight);
                }

                responsePassengers.setReservedSeats(responsePassengers.getReservedSeats() + reservation.getReservedSeats());
                iPassengersRepository.save(responsePassengers);

                reservation.setUser(user);
                reservation.setFlight(flight);
                return reservationRepository.save(reservation);
            } else {
                throw new IllegalArgumentException("User doesn't have enough money");
            }
        } else {
            throw new IllegalArgumentException("There are no available seats");
        }
    }


    public String returnReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow();
        double userSumMoney = reservation.getUser().getWallet().getEuro() + (reservation.getFlight().getCostEuro() * reservation.getReservedSeats());
        int sumSeats = reservation.getFlight().getPassengers().getReservedSeats() - reservation.getReservedSeats();
        reservation.getFlight().getPassengers().setReservedSeats(sumSeats);
        if(sumSeats <= reservation.getFlight().getPassengers().getCapacity()) {
            reservation.getFlight().setAvailableSeat(true);
            iFlightRepository.save(reservation.getFlight());
            iPassengersRepository.save(reservation.getFlight().getPassengers());
            reservation.getUser().getWallet().setEuro(userSumMoney);
            iWalletRepository.save(reservation.getUser().getWallet());
            reservationRepository.deleteById(reservationId);
        }
        return "Has been returned " + userSumMoney + " euro.";
    }


    public Reservation updateReservation(Long id, Reservation reservation) {
        Optional<Reservation> existingReservationOpt = reservationRepository.findById(id);
        if (existingReservationOpt.isPresent()) {
            Reservation existingReservation = existingReservationOpt.get();
            existingReservation.setFlight(reservation.getFlight());
            existingReservation.setUser(reservation.getUser());
            return reservationRepository.save(existingReservation);
        } else {
            return null;
        }
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
