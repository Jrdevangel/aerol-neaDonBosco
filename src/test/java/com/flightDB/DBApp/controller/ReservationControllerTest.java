package com.flightDB.DBApp.controller;


import com.flightDB.DBApp.controller.ReservationController;
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

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ReservationControllerTest {

    @Mock
    private ReservationService reservationService;

    private MockMvc mockMvc;

    @InjectMocks
    private ReservationController reservationController;

    private ObjectMapper objectMapper = new ObjectMapper();

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
        when(reservationService.buyReservation(any(Reservation.class))).thenReturn(reservation);

        String reservationJson = objectMapper.writeValueAsString(reservation);
        mockMvc.perform(post("/api/v1/new/reservation")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reservationJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.reservedSeats").value(3));
    }

    @Test
    void returnReservation() throws Exception {
        when(reservationService.returnReservation(1L)).thenReturn("Has been returned 50.0 euro.");
        mockMvc.perform(put("/api/v1/return/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Has been returned 50.0 euro."));
    }

    @Test
    void getReservationById() throws Exception {
        Reservation reservation = new Reservation();
        reservation.setId(1L);
        reservation.setReservedSeats(2);
        when(reservationService.getReservationById(1L)).thenReturn(Optional.of(reservation));

        mockMvc.perform(get("/api/v1/reservation/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.reservedSeats").value(2));
    }

    @Test
    void getAllReservations() throws Exception {
        Reservation reservation1 = new Reservation();
        reservation1.setId(1L);
        Reservation reservation2 = new Reservation();
        reservation2.setId(2L);
        when(reservationService.getAllReservations()).thenReturn(Arrays.asList(reservation1, reservation2));

        mockMvc.perform(get("/api/v1/reservation"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    void updateReservation() throws Exception {
        Reservation updatedReservation = new Reservation();
        updatedReservation.setId(1L);
        updatedReservation.setReservedSeats(4);
        when(reservationService.updateReservation(eq(1L), any(Reservation.class))).thenReturn(updatedReservation);

        String reservationJson = objectMapper.writeValueAsString(updatedReservation);
        mockMvc.perform(put("/api/v1/update/reservation/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(reservationJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.reservedSeats").value(4));
    }

    @Test
    void deleteReservation() throws Exception {
        mockMvc.perform(delete("/api/v1/delete/reservation/1"))
                .andExpect(status().isNoContent());

        verify(reservationService, times(1)).deleteReservation(1L);
    }

    @Test
    void getAllReservationByUserId() throws Exception {
        Reservation reservation1 = new Reservation();
        reservation1.setId(1L);
        Reservation reservation2 = new Reservation();
        reservation2.setId(2L);
        when(reservationService.getAllReservationByUserId(1L)).thenReturn(Arrays.asList(reservation1, reservation2));

        mockMvc.perform(get("/api/v1/reservation/user/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[1].id").value(2L));
    }
}