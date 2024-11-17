package com.flightDB.DBApp.service;

import com.flightDB.DBApp.dtos.request.FlightDataToBuyDTO;
import com.flightDB.DBApp.dtos.request.RequestBoughtDataDTO;
import com.flightDB.DBApp.dtos.response.ResponseToConfirmDTO;
import com.flightDB.DBApp.dtos.response.SeatAndPlaneDTO;
import com.flightDB.DBApp.model.*;
import com.flightDB.DBApp.repository.ISeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SeatsService {

    @Autowired private ISeatRepository iSeatRepository;
    @Autowired private WalletService walletService;
    @Autowired private UserService userService;
    @Autowired private FlightsService flightsService;
    @Autowired private HistoryOfPaymentService historyOfPaymentService;

    public List<Seats> getAllSeatsByFlightId(Long flightId) {
        return iSeatRepository.findByFlightId(flightId);
    }
    public List<SeatAndPlaneDTO> getAllSeatsByUserId(Long userId) {
        List<SeatAndPlaneDTO> seatAndPlaneDTOS = new ArrayList<>();
        List<Seats> seatsList = iSeatRepository.findByUserId(userId);
        Set<Flight> flightSet = new HashSet<>();
        for (Seats seat : seatsList) {
            flightSet.add(seat.getFlight());
        }
        for (Flight flight : flightSet) {
            List<Seats> relatedSeats = new ArrayList<>();
            for (Seats seat : seatsList) {
                if (seat.getFlight().equals(flight)) {
                    relatedSeats.add(seat);
                }
            }
            SeatAndPlaneDTO dto = new SeatAndPlaneDTO(flight, relatedSeats);
            seatAndPlaneDTOS.add(dto);
        }
        return seatAndPlaneDTOS;
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
                seatToAddUserId.setAvailable(false);
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
        historyOfPayment.setDirection(flight.getOrigin() + " -> " + flight.getDestination());
        historyOfPayment.setUser(user);
        String seatsBought = seatsList.stream()
                .map(Seats::getSeatName)
                .collect(Collectors.joining(","));
        historyOfPayment.setSeatsBought(seatsBought);

        historyOfPayment.setStatus(status);
        historyOfPayment.setTotalPayedMoney(totalCost);
        historyOfPaymentService.saveHistoryOFPayment(historyOfPayment);
    }


    public ResponseToConfirmDTO cancelBought(RequestBoughtDataDTO requestBoughtDataDTO) {
        Seats seats = getSeatById(requestBoughtDataDTO.getSeatId());
        ResponseToConfirmDTO responseToConfirmDTO = new ResponseToConfirmDTO();

        if (LocalDateTime.now().isAfter(seats.getFlight().getDepartureTime())) {
            throw new IllegalArgumentException("You can't return because the time has expired");
        }

        if (LocalDateTime.now().isAfter(seats.getFlight().getDepartureTime().minusDays(1))) {
            double refundAmount = seats.getCostOfSeat() / 2;
            if (!requestBoughtDataDTO.isConfirmed()) {
                responseToConfirmDTO.setText("You'll return this money: " + refundAmount + ". Please confirm if you want to continue");
                responseToConfirmDTO.setTotalReturn(false);
                return responseToConfirmDTO;
            }
            returnMoney(refundAmount, requestBoughtDataDTO.getUserId(), responseToConfirmDTO, seats);
        } else {
            returnMoney(seats.getCostOfSeat(), requestBoughtDataDTO.getUserId(), responseToConfirmDTO, seats);
        }

        return responseToConfirmDTO;
    }

    private void returnMoney(double moneyToReturn, Long userId, ResponseToConfirmDTO responseToConfirmDTO, Seats seats) {
        User user = userService.getUserById(userId);
        Wallet wallet = walletService.getByUserId(userId);

        HistoryOfPayment historyOfPayment = new HistoryOfPayment();
        historyOfPayment.setStatus(EStatus.Canceled);
        historyOfPayment.setTotalPayedMoney(moneyToReturn);
        historyOfPayment.setUser(user);
        historyOfPayment.setDirection(seats.getFlight().getOrigin().getCity() + " - " +
                seats.getFlight().getDestination().getCity());

        wallet.setEuro(wallet.getEuro() + moneyToReturn);
        walletService.saveWallet(wallet);

        seats.setUser(null);
        seats.setAvailable(true);
        saveSeat(seats);

        historyOfPaymentService.saveHistoryOFPayment(historyOfPayment);

        responseToConfirmDTO.setText("You have returned this money: " + moneyToReturn);
        responseToConfirmDTO.setTotalReturn(true);
    }

    private void saveAllSeats(List<Seats> seatsList) {
        iSeatRepository.saveAll(seatsList);
    }
    private void saveSeat(Seats seats) {
        iSeatRepository.save(seats);
    }
}
