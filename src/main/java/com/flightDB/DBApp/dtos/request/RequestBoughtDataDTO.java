package com.flightDB.DBApp.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RequestBoughtDataDTO {

    private Long seatId;
    private Long userId;
    private boolean isConfirmed;
}
