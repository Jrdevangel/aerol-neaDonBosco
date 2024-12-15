package com.flightDB.DBApp.model;

public class SeatsUtils {

    public static Long flight(Seats seats) {
        if (seats.getFlight() != null) {
            return seats.getFlight().getId();
        }
        return null;
    }
}