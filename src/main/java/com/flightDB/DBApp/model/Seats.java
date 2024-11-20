package com.flightDB.DBApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
public class Seats {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String seatName;

    @Column
    private boolean isAvailable;

    @Column
    private double costOfSeat;

    @Column
    private float discount;

    @ManyToOne
    @JoinColumn(name = "reserved_by_user_id", nullable = true)
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "flight_id")
    @JsonIgnore
    private Flight flight;
}
