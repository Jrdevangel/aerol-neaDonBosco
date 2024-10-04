package com.flightDB.DBApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
public class Routes {

    public Routes() {}

    public Routes(Long id, String country, String city) {
        this.id = id;
        this.country = country;
        this.city = city;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty("id")
    private Long id;

    @Column
    @JsonProperty("country")
    private String country;

    @Column
    @JsonProperty("city")
    private String city;

    @OneToMany(mappedBy = "destination", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Flight> destinationFlights;

    @OneToMany(mappedBy = "origin", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<Flight> originFlights;
}
