package com.flightDB.DBApp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
public class Flight {

    public Flight() {}

    public Flight(Long id, LocalDate departureTime, Routes destination, Routes origin, Passengers passengers) {
        this.id = id;
        this.departureTime = departureTime;
        this.destination = destination;
        this.origin = origin;
        this.passengers = passengers;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true)
    private LocalDate departureTime;

    @ManyToOne(optional = true)
    @JoinColumn(name = "destination_ID", nullable = true)
    @JsonIgnoreProperties({"originFlights", "destinationFlights"})
    private Routes destination;

    @ManyToOne(optional = true)
    @JoinColumn(name = "origin_ID", nullable = true)
    @JsonIgnoreProperties({"originFlights", "destinationFlights"})
    private Routes origin;

    @ManyToOne(optional = true)
    @JoinColumn(name = "passengers_ID", nullable = true)
    private Passengers passengers;
}
