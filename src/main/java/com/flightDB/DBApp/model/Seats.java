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
    @JsonIgnore
    private double costOfSeat;

    @Column
    @JsonIgnore
    private float discount;

    @ManyToOne
    @JoinColumn(name = "reserved_by_user_id", nullable = true)
    @JsonIgnore
    private User user;

    @ManyToOne
    @JoinColumn(name = "flight_id", nullable = false)
    @JsonIgnore
    private Flight flight;
}
