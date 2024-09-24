package com.flightDB.DBApp.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Locale;

@Data
@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table
public class Routes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "Country")
    private String country;

    @Column(name = "City")
    private String city;

}
