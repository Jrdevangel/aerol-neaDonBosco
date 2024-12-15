package com.flightDB.DBApp.repository;

import com.flightDB.DBApp.model.HistoryOfPayment;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IHistoryOfPaymentRepository extends CrudRepository<HistoryOfPayment, Long> {
    List<HistoryOfPayment> findAllByUserId(Long userId);
}