package com.flightDB.DBApp.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HistoryOfPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private EStatus status;

    @Column
    private String direction;

    @Column
    private String seatsBought;

    @Column
    private double totalPayedMoney;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
