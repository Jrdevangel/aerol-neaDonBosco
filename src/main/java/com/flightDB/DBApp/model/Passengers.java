package com.flightDB.DBApp.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Data
@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Passengers")
public class Passengers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "Capacity")
    private String capacity;

    @Column(name = "ReservedSeats")
    private String reservedSeats;

    @OneToMany(mappedBy = "passengers", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference(value = "passengers-reference")
    private Set<Flight> flights;
}
