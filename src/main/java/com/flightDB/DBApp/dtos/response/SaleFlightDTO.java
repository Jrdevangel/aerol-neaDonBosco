package com.flightDB.DBApp.dtos.response;

import com.flightDB.DBApp.model.Flight;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SaleFlightDTO {

    private Flight flight;
    private Float percentage;
    private double newPrice;
}
