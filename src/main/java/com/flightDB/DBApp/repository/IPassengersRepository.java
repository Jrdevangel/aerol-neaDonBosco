package com.flightDB.DBApp.repository;

import com.flightDB.DBApp.model.Passengers;
import org.springframework.data.repository.CrudRepository;

public interface IPassengersRepository extends CrudRepository<Passengers, Long> {
}
