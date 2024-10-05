package com.flightDB.DBApp.repository;

import com.flightDB.DBApp.model.Reservation;
import com.flightDB.DBApp.model.Wallet;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface IReservationRepository extends CrudRepository<Reservation, Long> {
    List<Reservation> findByUserId(Long userId);
}
