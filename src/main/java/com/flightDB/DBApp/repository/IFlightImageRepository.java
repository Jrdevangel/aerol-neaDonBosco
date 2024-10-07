package com.flightDB.DBApp.repository;

import com.flightDB.DBApp.model.FlightImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IFlightImageRepository extends JpaRepository<FlightImage, Long> {
    List<FlightImage> findByFlightId(Long flightId);
}
