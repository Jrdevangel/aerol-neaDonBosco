package com.flightDB.DBApp.repository;

import com.flightDB.DBApp.model.Seats;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ISeatRepository extends CrudRepository<Seats, Long> {
    List<Seats> findByUserId(Long userId);
    List<Seats> findByFlightId(Long flightId);
    void deleteByFlightId(Long flightId);

}
