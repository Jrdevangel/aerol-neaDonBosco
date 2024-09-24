package com.flightDB.DBApp.repository;

import com.flightDB.DBApp.model.Reservation;
import org.springframework.data.repository.CrudRepository;

public interface IReservationRepository extends CrudRepository<Reservation, Long> {
}
