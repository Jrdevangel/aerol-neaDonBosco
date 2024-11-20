package com.flightDB.DBApp.controller;

import com.flightDB.DBApp.service.HistoryOfPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class HistoryOfPaymentController {

    @Autowired private HistoryOfPaymentService historyOfPaymentService;
}
