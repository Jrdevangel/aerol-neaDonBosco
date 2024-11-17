package com.flightDB.DBApp.service;

import com.flightDB.DBApp.model.HistoryOfPayment;
import com.flightDB.DBApp.repository.IHistoryOfPaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HistoryOfPaymentService {

    @Autowired private IHistoryOfPaymentRepository iHistoryOfPaymentRepository;

    public List<HistoryOfPayment> getAllHistoryOfPaymentByUserId(Long userId) {
        return iHistoryOfPaymentRepository.findAllByUserId(userId);
    }

    public void saveAllHistoryOfPayment(List<HistoryOfPayment> historyOfPaymentList) {
        iHistoryOfPaymentRepository.saveAll(historyOfPaymentList);
    }
    public void saveHistoryOFPayment(HistoryOfPayment historyOfPayment) {
        iHistoryOfPaymentRepository.save(historyOfPayment);
    }
}
