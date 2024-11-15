package com.flightDB.DBApp.repository;

import com.flightDB.DBApp.model.Flight;
import com.flightDB.DBApp.model.Routes;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IFlightRepository extends CrudRepository<Flight, Long> {
    List<Flight> findByOriginIdAndDestinationId(Long origin, Long destination);
}
