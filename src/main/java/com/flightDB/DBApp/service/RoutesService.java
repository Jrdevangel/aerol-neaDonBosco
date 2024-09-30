// src/main/java/com/flightDB/DBApp/service/RoutesService.java

package com.flightDB.DBApp.service;

import com.flightDB.DBApp.model.Routes;
import com.flightDB.DBApp.repository.IRoutesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class RoutesService {

    private final IRoutesRepository routesRepository;

    @Autowired
    public RoutesService(IRoutesRepository routesRepository) {
        this.routesRepository = routesRepository;
    }

    // Create a new route
    public Routes createRoute(Routes route) {
        return routesRepository.save(route);
    }

    // Retrieve a route by ID
    public Optional<Routes> getRouteById(Long id) {
        return routesRepository.findById(id);
    }

    // Retrieve all routes
    public List<Routes> getAllRoutes() {
        return StreamSupport.stream(routesRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    // Update an existing route
    public Routes updateRoute(Long id, Routes route) {
        Optional<Routes> existingRouteOpt = routesRepository.findById(id);
        if (existingRouteOpt.isPresent()) {
            Routes existingRoute = existingRouteOpt.get();
            existingRoute.setCountry(route.getCountry());
            existingRoute.setCity(route.getCity());
            existingRoute.setDestinationFlights(route.getDestinationFlights());
            existingRoute.setOriginFlights(route.getOriginFlights());
            return routesRepository.save(existingRoute);
        } else {
            return null; // Or handle as needed
        }
    }

    // Delete a route by ID
    public void deleteRoute(Long id) {
        routesRepository.deleteById(id);
    }
}