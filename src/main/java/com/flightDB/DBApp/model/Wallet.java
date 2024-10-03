package com.flightDB.DBApp.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private double euro;

    @OneToOne(optional = false)
    @JoinColumn(name = "user_ID", nullable = false)
    @JsonBackReference(value = "user-wallet-reference")
    private User user;

}
