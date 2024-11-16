package com.flightDB.DBApp.service;

import com.flightDB.DBApp.dtos.request.FlightDataToBuyDTO;
import com.flightDB.DBApp.model.Flight;
import com.flightDB.DBApp.model.Seats;
import com.flightDB.DBApp.model.User;
import com.flightDB.DBApp.model.Wallet;
import com.flightDB.DBApp.repository.ISeatRepository;
import com.flightDB.DBApp.repository.IWalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SeatsService {

    @Autowired private ISeatRepository iSeatRepository;
    @Autowired private WalletService walletService;
    @Autowired private UserService userService;
    @Autowired private FlightsService flightsService;

    private List<Seats> getAllSeatsByFlightId(Long flightId) {
        return iSeatRepository.findByFlightId(flightId);
    }
    private List<Seats> getAllSeatsByUserId(Long userId) {
        return iSeatRepository.findByUserId(userId);
    }

    public String buySeatsInThePlane(FlightDataToBuyDTO flightDataToBuyDTO) {
        List<Seats> allSeatsOfFlightList = getAllSeatsByFlightId(flightDataToBuyDTO.getFlightId());
        List<Seats> seatsSelected = new ArrayList<>();
        for(Seats seatOfFlight : allSeatsOfFlightList) {
            for (Long seatsId : flightDataToBuyDTO.getSeatIdList()) {
                if (seatOfFlight.getId() == seatsId) {
                    seatsSelected.add(seatOfFlight);
                }
            }
        }
        Wallet userWallet = walletService.getByUserId(flightDataToBuyDTO.getUserId());
        double totalCost = 0;
        for (Seats seatToBuy : seatsSelected) {
            if(seatToBuy.getDiscount() < 0) {
                throw new IllegalArgumentException("Discount is less 0");
            }
            if(seatToBuy.getDiscount() == 0) {
                totalCost += seatToBuy.getCostOfSeat();
            } else {
                totalCost += seatToBuy.getCostOfSeat() - seatToBuy.getDiscount();
            }
        }
        User user = userService.getUserById(flightDataToBuyDTO.getUserId());
        double actualMoney = userWallet.getEuro();
        if(actualMoney >= totalCost) {
            for (Seats seatToAddUserId : seatsSelected) {
                seatToAddUserId.setUser(user);
            }
            saveAllSeats(seatsSelected);
            userWallet.setEuro(actualMoney - totalCost);
            walletService.saveWallet(userWallet);
            Flight flight = flightsService.getFlightById(flightDataToBuyDTO.getFlightId());
            flight.setReservedSeats(flight.getReservedSeats() + seatsSelected.size());
            flightsService.saveFlight(flight);
            return "Successful";
        } else {
            throw new IllegalArgumentException("You don't have enough money");
        }
    }

    private void saveAllSeats(List<Seats> seatsList) {
        iSeatRepository.saveAll(seatsList);
    }
}
