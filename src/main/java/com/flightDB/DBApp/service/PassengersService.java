// src/main/java/com/flightDB/DBApp/service/PassengersService.java

package com.flightDB.DBApp.service;

import com.flightDB.DBApp.model.Passengers;
import com.flightDB.DBApp.repository.IPassengersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PassengersService {

    private final IPassengersRepository passengersRepository;

    @Autowired
    public PassengersService(IPassengersRepository passengersRepository) {
        this.passengersRepository = passengersRepository;
    }

    // Create a new passenger
    public Passengers createPassenger(Passengers passenger) {
        return passengersRepository.save(passenger);
    }

    // Get a passenger by ID
    public Optional<Passengers> getPassengerById(Long id) {
        return passengersRepository.findById(id);
    }

    // Get all passengers
    public List<Passengers> getAllPassengers() {
        return StreamSupport.stream(passengersRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    // Update a passenger
    public Passengers updatePassenger(Long id, Passengers passenger) {
        Optional<Passengers> existingPassengerOpt = passengersRepository.findById(id);
        if (existingPassengerOpt.isPresent()) {
            Passengers existingPassenger = existingPassengerOpt.get();
            existingPassenger.setCapacity(passenger.getCapacity());
            existingPassenger.setReservedSeats(passenger.getReservedSeats());
            existingPassenger.setFlights(passenger.getFlights());
            return passengersRepository.save(existingPassenger);
        } else {
            return null; // Or handle as needed
        }
    }

    // Delete a passenger by ID
    public void deletePassenger(Long id) {
        passengersRepository.deleteById(id);
    }
}
