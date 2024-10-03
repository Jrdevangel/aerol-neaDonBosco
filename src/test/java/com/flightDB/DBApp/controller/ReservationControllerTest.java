package com.flightDB.DBApp.controller;

import com.flightDB.DBApp.model.Reservation;
import com.flightDB.DBApp.service.ReservationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ReservationControllerTest {

    @Mock
    private ReservationService reservationService;

    private MockMvc mockMvc;

    @InjectMocks
    private ReservationController reservationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(reservationController).build();
    }

    @Test
    void createReservation() throws Exception {
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setReservedSeats(3);
        when(reservationService.createReservation(any(Reservation.class))).thenReturn(reservation);
        ObjectMapper objectMapper = new ObjectMapper();
        String reservationJson = objectMapper.writeValueAsString(reservation);
        mockMvc.perform(post("/api/v1/new/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reservationJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.reservedSeats").value(3));
    }
}
