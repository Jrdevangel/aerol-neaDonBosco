package com.flightDB.DBApp.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    @JoinColumn(name = "passengers_ID", nullable = false)
    private Passengers passengers;

    @Column(nullable = false)
    private double costEuro;

    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FlightImage> images = new ArrayList<>(); // Initialize to an empty list

    public Flight(Long id, LocalDateTime departureTime, Routes destination, Routes origin, Passengers passengers, double costEuro) {
        this.id = id;
        this.departureTime = departureTime;
        this.destination = destination;
        this.origin = origin;
        this.passengers = passengers;
        this.costEuro = costEuro;
        this.images = new ArrayList<>(); // Ensure images is initialized
    }
}
