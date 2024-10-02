// src/main/java/com/flightDB/DBApp/service/ReservationService.java

package com.flightDB.DBApp.service;

import com.flightDB.DBApp.model.Passengers;
import com.flightDB.DBApp.model.Reservation;
import com.flightDB.DBApp.repository.IPassengersRepository;
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

    @Autowired
    public ReservationService(IReservationRepository reservationRepository, IPassengersRepository iPassengersRepository) {
        this.reservationRepository = reservationRepository;
        this.iPassengersRepository = iPassengersRepository;
    }


    public Reservation createReservation(Reservation reservation, int passengers) {
        Passengers responsePassengers = reservation.getFlight().getPassengers();
        if(responsePassengers.getReservedSeats() + passengers <= responsePassengers.getCapacity()) {
            responsePassengers.setReservedSeats(responsePassengers.getReservedSeats() + passengers);
            iPassengersRepository.save(responsePassengers);
            return reservationRepository.save(reservation);
        } else {
            throw new IllegalArgumentException("There is no available seats");
        }
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
