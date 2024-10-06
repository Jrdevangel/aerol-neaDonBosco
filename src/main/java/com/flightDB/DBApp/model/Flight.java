package com.flightDB.DBApp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Blob;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime departureTime;

    @Column
    private boolean availableSeat;

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

    @Column(nullable = false)
    private double costEuro;

    @Column
    private Blob photo;

    public Flight(Long id, LocalDateTime departureTime, Routes destination, Routes origin, Passengers passengers, double costEuro) {
        this.id = id;
        this.departureTime = departureTime;
        this.destination = destination;
        this.origin = origin;
        this.passengers = passengers;
        this.costEuro = costEuro;
    }
}
