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

    private Routes getRouteByCountryAndCity(String country, String city) {
        return routesRepository.findByCountryAndCity(country, city);
    }
    public Routes createRoute(Routes route) {
        Routes routes = getRouteByCountryAndCity(route.getCountry(), route.getCity());
        if(routes != null) {
            new IllegalArgumentException("This route is already exists");
        }
        return routesRepository.save(route);
    }

    public Optional<Routes> getRouteById(Long id) {
        return routesRepository.findById(id);
    }

    public List<Routes> getAllRoutes() {
        return StreamSupport.stream(routesRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

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
            return null;
        }
    }

    public void deleteRoute(Long id) {
        routesRepository.deleteById(id);
    }
}
