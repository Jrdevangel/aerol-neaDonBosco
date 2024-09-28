package com.flightDB.DBApp.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Entity
@Getter
@Setter
public class Passengers {
    public Passengers(Long id, int capacity, int reservedSeats) {
        this.id = id;
        this.capacity = capacity;
        this.reservedSeats = reservedSeats;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Capacity")
    private int capacity;

    @Column(name = "ReservedSeats")
    private int reservedSeats;

    @OneToMany(mappedBy = "passengers", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "passengers-reference")
    private Set<Flight> flights;
}
