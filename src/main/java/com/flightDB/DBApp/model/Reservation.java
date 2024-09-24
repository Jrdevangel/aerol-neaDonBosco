package com.flightDB.DBApp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "flight_ID", nullable = false)
    @JsonBackReference(value = "flight-reservation-reference")
    private Flight flight;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_ID", nullable = false)
    @JsonBackReference(value = "user-reservation-reference")
    private User user;
}
