package com.flightDB.DBApp.service;

import com.flightDB.DBApp.model.HistoryOfPayment;
import com.flightDB.DBApp.repository.IHistoryOfPaymentRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.List;

public class HistoryOfPaymentService {

    private final IHistoryOfPaymentRepository iHistoryOfPaymentRepository;

    public HistoryOfPaymentService(IHistoryOfPaymentRepository iHistoryOfPaymentRepository) {
        this.iHistoryOfPaymentRepository = iHistoryOfPaymentRepository;
    }

    @GetMapping("/user/{userID}")
    public List<HistoryOfPayment> getAllHistoryOfPaymentByUserId( @PathVariable Long userId) {
        return iHistoryOfPaymentRepository.findAllByUserId(userId);
    }

    public void saveAllHistoryOfPayment(List<HistoryOfPayment> historyOfPaymentList) {
        iHistoryOfPaymentRepository.saveAll(historyOfPaymentList);
    }

    public void saveHistoryOFPayment(HistoryOfPayment historyOfPayment) {
        iHistoryOfPaymentRepository.save(historyOfPayment);
    }

    public void deleteSeat(Long seatId) {
        if (!iHistoryOfPaymentRepository.existsById(seatId)) {
            throw new IllegalArgumentException("Seat with ID " + seatId + " doesn't exist");
        }
        iHistoryOfPaymentRepository.deleteById(seatId);
    }

    public List<HistoryOfPayment> findAllByUserId(Long userId) {
        return iHistoryOfPaymentRepository.findAllByUserId(userId);
    }
}
