package com.flightDB.DBApp.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SeatDTO {

    private Double costOfSeat;
    private float discount;
    private String seatName;
    private Boolean isAvailable;
    private Long reservedByUserId;
    private Long flightId;
}
