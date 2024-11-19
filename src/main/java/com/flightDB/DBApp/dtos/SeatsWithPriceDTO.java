package com.flightDB.DBApp.dtos;

import com.flightDB.DBApp.model.Seats;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SeatsWithPriceDTO {

    private Seats seats;
    private double originalPrice;
    private double discountedPrice;
    private float percentage;
}
