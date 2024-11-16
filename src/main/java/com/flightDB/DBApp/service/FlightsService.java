package com.flightDB.DBApp.service;

import com.flightDB.DBApp.dtos.FilteredRoutesDTO;
import com.flightDB.DBApp.dtos.request.FlightSearchDataDTO;
import com.flightDB.DBApp.dtos.response.SaleFlightDTO;
import com.flightDB.DBApp.model.Flight;
import com.flightDB.DBApp.model.Routes;
import com.flightDB.DBApp.repository.IFlightRepository;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class FlightsService {

    @Autowired private IFlightRepository iFlightRepository;

    @Autowired private RoutesService routesService;

    public List<Flight> getAllFlight() {
        return (List<Flight>) iFlightRepository.findAll();
    }

    public void deleteFright(Long id) {
        iFlightRepository.deleteById(id);
    }

    public Flight updateFlight(Long id, Flight newFlight) {
        newFlight.setId(id);
        return iFlightRepository.save(newFlight);
    }
    private FilteredRoutesDTO filterText(FlightSearchDataDTO flightSearchDataDTO) {
        List<Routes> routesList = routesService.getAllRoutes();
        LevenshteinDistance levenshteinDistance = new LevenshteinDistance();
        List<Routes> originRoutesList = new ArrayList<>();
        List<Routes> destinyRoutesList = new ArrayList<>();

        Routes bestOriginMatch = null;
        Routes bestDestinationMatch = null;

        int bestOriginDistance = Integer.MAX_VALUE;
        int bestDestinationDistance = Integer.MAX_VALUE;

        for (Routes routes : routesList) {
            int originCountryDistance = levenshteinDistance.apply(routes.getCountry().toLowerCase(), flightSearchDataDTO.getOriginCountry().toLowerCase());
            int originCityDistance = levenshteinDistance.apply(routes.getCity().toLowerCase(), flightSearchDataDTO.getOriginCity().toLowerCase());

            int destinationCountryDistance = levenshteinDistance.apply(routes.getCountry().toLowerCase(), flightSearchDataDTO.getDestinationCountry().toLowerCase());
            int destinationCityDistance = levenshteinDistance.apply(routes.getCity().toLowerCase(), flightSearchDataDTO.getDestinationCity().toLowerCase());

            if(originCountryDistance + originCityDistance < bestOriginDistance) {
                bestOriginDistance = originCountryDistance + originCityDistance;
                bestOriginMatch = routes;
            }

            if(destinationCountryDistance + destinationCityDistance < bestDestinationDistance) {
                bestDestinationDistance = destinationCountryDistance + destinationCityDistance;
                bestDestinationMatch = routes;
            }

            if(originCountryDistance + originCityDistance <= 2) {
                originRoutesList.add(routes);
            }

            if(destinationCountryDistance + destinationCityDistance <= 2) {
                destinyRoutesList.add(routes);
            }
        }
        originRoutesList.add(0,bestOriginMatch);
        destinyRoutesList.add(0, bestDestinationMatch);
        return new FilteredRoutesDTO(originRoutesList, destinyRoutesList);
    }
    public List<Flight> getAllFlightBySearch(FlightSearchDataDTO flightSearchDataDTO) {
        FilteredRoutesDTO filteredRoutesDTO = filterText(flightSearchDataDTO);
        Set<Flight> filteredFlightsSet = new HashSet<>();

        for (Routes originRoute : filteredRoutesDTO.getOriginRoutesList()) {
            for (Routes destinyRoute : filteredRoutesDTO.getDestinationRoutesList()) {
                List<Flight> flightList = iFlightRepository.findByOriginIdAndDestinationId(originRoute.getId(), destinyRoute.getId());
                LocalDate startDate = flightSearchDataDTO.getStartLocalDate();
                LocalDate finishDate = flightSearchDataDTO.getFinishLocalDate();

                for (Flight flight : flightList) {
                    LocalDateTime departureTime = flight.getDepartureTime();
                    boolean matchesStartDate = (startDate == null || !departureTime.isBefore(startDate.atStartOfDay()));
                    boolean matchesFinishDate = (finishDate == null || !departureTime.isAfter(finishDate.atStartOfDay()));

                    if (matchesStartDate && matchesFinishDate) {
                        if (flight.getReservedSeats() < flight.getCapacity()) {
                            filteredFlightsSet.add(flight);
                        }
                    }
                }
            }
        }
        return new ArrayList<>(filteredFlightsSet);
    }

//    public List<SaleFlightDTO> getAllFlightWhatIsOnSale() {
//        List<Flight> flightList = iFlightRepository.findByOnSaleNumberOfMoneyGreaterThan(20L);
//        List<SaleFlightDTO> flightsHasTheBiggestSaleList = new ArrayList<>();
//        for (Flight flight : flightList) {
//            float percentage = countPercentage(flight.getCostEuro(), flight.getOnSaleNumberOfMoney());
//            double newCost = countNewPrice(flight.getCostEuro(), flight.getOnSaleNumberOfMoney());
//            flightsHasTheBiggestSaleList.add(new SaleFlightDTO(flight, percentage, newCost));
//        }
//        return flightsHasTheBiggestSaleList.stream()
//                .sorted(Comparator.comparingDouble(SaleFlightDTO::getPercentage)
//                        .reversed()).collect(Collectors.toList());
//    }

    private float countPercentage(double cost, double salePrice) {
        if (cost == 0) {
            return 0;
        }
        return (float) ((cost - salePrice) / cost * 100);
    }
    private double countNewPrice(double cost, double salePrice) {
        if(cost < salePrice) {
            new IllegalArgumentException("Sale price is major than cost error");
        }
        return cost - salePrice;
    }

    public Flight getFlightById(Long id) {
        return iFlightRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Flight not found"));
    }


    public Flight createFlight(Flight flight) {
        return iFlightRepository.save(flight);
    }


    public void saveFlight(Flight flight) {
        iFlightRepository.save(flight);
    }


}
