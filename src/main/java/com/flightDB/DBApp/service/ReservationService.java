// src/main/java/com/flightDB/DBApp/service/ReservationService.java

package com.flightDB.DBApp.service;

import com.flightDB.DBApp.model.Reservation;
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

    @Autowired
    public ReservationService(IReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }


    public Reservation createReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
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
