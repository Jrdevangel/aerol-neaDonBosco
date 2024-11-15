package com.flightDB.DBApp.repository;

import com.flightDB.DBApp.model.Routes;
import org.springframework.data.repository.CrudRepository;

public interface IRoutesRepository extends CrudRepository<Routes, Long> {
    Routes findByCountryAndCity(String country, String city);
}
