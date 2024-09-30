package com.flightDB.DBApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
public class Passengers {

    public Passengers() {}

    public Passengers(Long id, int capacity, int reservedSeats) {
        this.id = id;
        this.capacity = capacity;
        this.reservedSeats = reservedSeats;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private int capacity;

    @Column
    private int reservedSeats;

    @OneToMany(mappedBy = "passengers", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Flight> flights;
}
