// src/main/java/com/flightDB/DBApp/service/ReservationService.java

package com.flightDB.DBApp.service;

import com.flightDB.DBApp.model.Flight;
import com.flightDB.DBApp.model.Passengers;
import com.flightDB.DBApp.model.Reservation;
import com.flightDB.DBApp.repository.IFlightRepository;
import com.flightDB.DBApp.repository.IPassengersRepository;
import com.flightDB.DBApp.repository.IReservationRepository;
import com.flightDB.DBApp.repository.IWalletRepository;
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

    @Autowired
    public ReservationService(IReservationRepository reservationRepository, IPassengersRepository iPassengersRepository, IFlightRepository iFlightRepository, IWalletRepository iWalletRepository) {
        this.reservationRepository = reservationRepository;
        this.iPassengersRepository = iPassengersRepository;
        this.iFlightRepository = iFlightRepository;
        this.iWalletRepository = iWalletRepository;
    }


    public Reservation buyReservation(Reservation reservation) {
        Passengers responsePassengers = reservation.getFlight().getPassengers();
        if(responsePassengers.getReservedSeats() + reservation.getReservedSeats() <= responsePassengers.getCapacity()) {
            if(reservation.getUser().getWallet().getEuro() >= reservation.getFlight().getCostEuro()) {
                double lessMoney = reservation.getUser().getWallet().getEuro() - (reservation.getFlight().getCostEuro() * reservation.getReservedSeats());
                reservation.getUser().getWallet().setEuro(lessMoney);
                iWalletRepository.save(reservation.getUser().getWallet());
                if (responsePassengers.getReservedSeats() + reservation.getReservedSeats() == responsePassengers.getCapacity()) {
                    Flight flight = reservation.getFlight();
                    flight.setAvailableSeat(true);
                    iFlightRepository.save(flight);
                }
                responsePassengers.setReservedSeats(responsePassengers.getReservedSeats() + reservation.getReservedSeats());
                iPassengersRepository.save(responsePassengers);
                return reservationRepository.save(reservation);
            } else {
                throw new IllegalArgumentException("User don't have enough money");
            }
        } else {
            throw new IllegalArgumentException("There is no available seats");
        }
    }

    public String returnReservation(Long reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId).orElseThrow();
        double userSumMoney = reservation.getUser().getWallet().getEuro() + (reservation.getFlight().getCostEuro() * reservation.getReservedSeats());
        int sumSeats = reservation.getFlight().getPassengers().getReservedSeats() - reservation.getReservedSeats();
        reservation.getFlight().getPassengers().setReservedSeats(sumSeats);
        if(sumSeats <= reservation.getFlight().getPassengers().getCapacity()) {
            reservation.getFlight().setAvailableSeat(false);
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
            // Update other fields if there are any
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
}
