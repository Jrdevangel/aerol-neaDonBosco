package com.flightDB.DBApp.controller;

import com.flightDB.DBApp.model.HistoryOfPayment;
import com.flightDB.DBApp.service.HistoryOfPaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/history-of-payment")
public class HistoryOfPaymentController {

    private HistoryOfPaymentService historyOfPaymentService;

    @DeleteMapping("/seat/{seatId}")
    public ResponseEntity<String> deleteSeat(@PathVariable("seatId") Long seatId) {
        try {
            historyOfPaymentService.deleteSeat(seatId);
            return ResponseEntity.ok("Seat with ID " + seatId + " successfully eliminated.");
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Failed to delete seat with ID " + seatId + ": " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete seat with ID " + seatId + ": " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<HistoryOfPayment>> getAllHistoryOfPaymentsByUserId(@PathVariable Long userId) {
        List<HistoryOfPayment> payments = historyOfPaymentService.findAllByUserId(userId);
        if (payments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(payments);
    }
}