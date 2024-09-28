package com.flightDB.DBApp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
public class Flight {
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

    @Column
    private LocalDate departureTime;

    @ManyToOne(optional = true)
    @JoinColumn(name = "destination_ID", nullable = true)
    @JsonBackReference(value = "destination-reference")
    private Routes destination;

    @ManyToOne(optional = true)
    @JoinColumn(name = "origin_ID", nullable = true)
    @JsonBackReference(value = "origin-reference")
    private Routes origin;

    @ManyToOne(optional = true)
    @JoinColumn(name = "passengers_ID", nullable = true)
    @JsonBackReference(value = "passengers-reference")
    private Passengers passengers;

    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "flight-reservation-reference")
    private Set<Reservation> reservations;

}
