package com.flightDB.DBApp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class FlightImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(nullable = false, length = 16 * 1024 * 1024) // Максимум 16 MB
    private byte[] imageData;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id", nullable = false)
    @JsonIgnore
    private Flight flight;

    public FlightImage(byte[] imageData, Flight flight) {
        this.imageData = imageData;
        this.flight = flight;
    }

    public boolean isImageEmpty() {
        return this.imageData == null || this.imageData.length == 0;
    }
}
