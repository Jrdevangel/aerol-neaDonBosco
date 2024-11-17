package com.flightDB.DBApp.dtos.response;

import com.flightDB.DBApp.model.Flight;
import com.flightDB.DBApp.model.Seats;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SeatAndPlaneDTO {

    private Flight flight;
    private List<Seats> seatsList;
}
