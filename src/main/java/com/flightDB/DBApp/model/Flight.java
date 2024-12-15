package com.flightDB.DBApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor

public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime departureTime;

    @ManyToOne(optional = true)
    @JoinColumn(name = "destination_ID", nullable = true)
    @JsonIgnoreProperties({"originFlights", "destinationFlights"})
    private Routes destination;

    @ManyToOne(optional = true)
    @JoinColumn(name = "origin_ID", nullable = true)
    @JsonIgnoreProperties({"originFlights", "destinationFlights"})
    private Routes origin;

    @Column
    private int capacity;

    @Column
    private int reservedSeats;

    @OneToMany(mappedBy = "flight", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FlightImage> images = new ArrayList<>();

}
