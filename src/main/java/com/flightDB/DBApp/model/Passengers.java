package com.flightDB.DBApp.model;

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
public class Passengers {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

}
