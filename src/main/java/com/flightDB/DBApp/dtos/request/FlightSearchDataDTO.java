package com.flightDB.DBApp.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FlightSearchDataDTO {

    private String originCountry;
    private String originCity;
    private String destinationCountry;
    private String destinationCity;
    private LocalDate startLocalDate;
    private LocalDate finishLocalDate;
}
