package com.flightDB.DBApp.service;

import com.flightDB.DBApp.dtos.request.FlightDataToBuyDTO;
import com.flightDB.DBApp.dtos.request.RequestBoughtDataDTO;
import com.flightDB.DBApp.dtos.response.ResponseToConfirmDTO;
import com.flightDB.DBApp.model.*;
import com.flightDB.DBApp.repository.ISeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class SeatsService {

    @Autowired private ISeatRepository iSeatRepository;
    @Autowired private WalletService walletService;
    @Autowired private UserService userService;
    @Autowired private FlightsService flightsService;
    @Autowired private HistoryOfPaymentService historyOfPaymentService;

    private List<Seats> getAllSeatsByFlightId(Long flightId) {
        return iSeatRepository.findByFlightId(flightId);
    }
    private List<Seats> getAllSeatsByUserId(Long userId) {
        return iSeatRepository.findByUserId(userId);
    }
    private Seats getSeatById(Long id) {
        return iSeatRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Seat is not found"));
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
        Flight flight = flightsService.getFlightById(flightDataToBuyDTO.getFlightId());
        User user = userService.getUserById(flightDataToBuyDTO.getUserId());
        double actualMoney = userWallet.getEuro();
        if(actualMoney >= totalCost) {
            for (Seats seatToAddUserId : seatsSelected) {
                seatToAddUserId.setUser(user);
            }
            saveAllSeats(seatsSelected);
            userWallet.setEuro(actualMoney - totalCost);
            walletService.saveWallet(userWallet);
            flight.setReservedSeats(flight.getReservedSeats() + seatsSelected.size());
            flightsService.saveFlight(flight);
            changeSeatsToHistoryOfPayment(seatsSelected, flight, user, totalCost, EStatus.Paid);
            return "Successful";
        } else {
            changeSeatsToHistoryOfPayment(seatsSelected, flight, user, totalCost, EStatus.Error);
            throw new IllegalArgumentException("You don't have enough money");
        }
    }

    private void changeSeatsToHistoryOfPayment(List<Seats> seatsList, Flight flight, User user, double totalCost, EStatus status) {
            HistoryOfPayment historyOfPayment = new HistoryOfPayment();
            historyOfPayment.setDirection(flight.getOrigin() + " " + flight.getDestination());
            historyOfPayment.setUser(user);
            historyOfPayment.setSeatsBought(seatsList.toString());
            historyOfPayment.setStatus(status);
            historyOfPayment.setTotalPayedMoney(- totalCost);
            historyOfPaymentService.saveHistoryOFPayment(historyOfPayment);
    }

    public ResponseToConfirmDTO cancelBought(RequestBoughtDataDTO requestBoughtDataDTO) {
        Seats seats = getSeatById(requestBoughtDataDTO.getSeatId());
        ResponseToConfirmDTO responseToConfirmDTO = new ResponseToConfirmDTO();
            if (LocalDateTime.now().isAfter(seats.getFlight().getDepartureTime())) {
                throw new IllegalArgumentException("You can't return because the time has expires");
            } else {
                if (LocalDateTime.now().isAfter(seats.getFlight().getDepartureTime().minusDays(1))) {
                    double newPrice = seats.getCostOfSeat() / 2;
                    if(!requestBoughtDataDTO.isConfirmed()) {
                        responseToConfirmDTO.setText("You'll return this money: " + newPrice + "Pleas confirm if you want to continue");
                        responseToConfirmDTO.setTotalReturn(false);
                    }
                    if(requestBoughtDataDTO.isConfirmed()) {
                      returnMoney(newPrice, requestBoughtDataDTO.getUserId(), responseToConfirmDTO, seats);
                } else {
                    double newPriceOriginal = seats.getCostOfSeat();
                    returnMoney(newPriceOriginal, requestBoughtDataDTO.getUserId(), responseToConfirmDTO, seats);
                }
            }
                }
        return responseToConfirmDTO;
    }

    private void returnMoney(double moneyToReturn, Long userId, ResponseToConfirmDTO responseToConfirmDTO, Seats seats) {
        User user = userService.getUserById(userId);
        Wallet wallet = walletService.getByUserId(user.getId());
        wallet.setEuro(wallet.getEuro() + moneyToReturn);
        walletService.saveWallet(wallet);
        seats.setUser(null);
        saveSeat(seats);
        responseToConfirmDTO.setText("You have returned this money: " + moneyToReturn);
        responseToConfirmDTO.setTotalReturn(true);
        HistoryOfPayment historyOfPayment = new HistoryOfPayment();
        historyOfPayment.setStatus(EStatus.Canceled);
        historyOfPayment.setTotalPayedMoney(moneyToReturn);
        historyOfPayment.setUser(seats.getUser());
        historyOfPayment.setDirection(seats.getFlight().getOrigin() + " " + seats.getFlight().getDestination());
        historyOfPaymentService.saveHistoryOFPayment(historyOfPayment);
    }

    private void saveAllSeats(List<Seats> seatsList) {
        iSeatRepository.saveAll(seatsList);
    }
    private void saveSeat(Seats seats) {
        iSeatRepository.save(seats);
    }
}
