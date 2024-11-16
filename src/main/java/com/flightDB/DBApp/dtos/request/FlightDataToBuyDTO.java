package com.flightDB.DBApp.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FlightDataToBuyDTO {

    private List<Long> seatIdList;
    private Long flightId;
    private Long userId;
}
