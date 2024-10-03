package com.flightDB.DBApp.repository;

import com.flightDB.DBApp.model.Flight;
import org.springframework.data.repository.CrudRepository;

public interface IFlightRepository extends CrudRepository<Flight, Long> {
}