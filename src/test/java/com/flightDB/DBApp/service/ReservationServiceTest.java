package com.flightDB.DBApp.service;


import com.flightDB.DBApp.model.Reservation;
import com.flightDB.DBApp.repository.IReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReservationServiceTest {

    @Mock
    private IReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test para crear una reserva
    @Test
    public void testCreateReservation() {
        Reservation reservation = new Reservation();  // Asegúrate de que tu modelo tenga un constructor vacío o usa lombok.
        when(reservationRepository.save(any(Reservation.class))).thenReturn(reservation);

        Reservation createdReservation = reservationService.createReservation(reservation);

        assertNotNull(createdReservation); // Verifica que la reserva no es nula
        verify(reservationRepository, times(1)).save(reservation); // Verifica que el método save fue llamado una vez
    }

    // Test para obtener una reserva por ID
    @Test
    public void testGetReservationById() {
        Reservation reservation = new Reservation();  // Crea un objeto mock de reserva
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(reservation));

        Optional<Reservation> foundReservation = reservationService.getReservationById(1L);

        assertTrue(foundReservation.isPresent()); // Verifica que se encontró la reserva
        verify(reservationRepository, times(1)).findById(1L); // Verifica que se llamó al método findById con el ID correcto
    }

    // Test para obtener todas las reservas
    @Test
    public void testGetAllReservations() {
        Reservation reservation1 = new Reservation();  // Mockea dos objetos de reserva
        Reservation reservation2 = new Reservation();
        List<Reservation> reservationsList = new ArrayList<>(List.of(reservation1, reservation2));

        when(reservationRepository.findAll()).thenReturn(reservationsList);  // Simula la respuesta del repositorio

        List<Reservation> foundReservations = reservationService.getAllReservations();

        assertEquals(2, foundReservations.size());  // Verifica que se obtuvieron dos reservas
        verify(reservationRepository, times(1)).findAll();  // Verifica que el método findAll fue llamado una vez
    }

    // Test para actualizar una reserva
    @Test
    public void testUpdateReservation() {
        Reservation existingReservation = new Reservation();  // Crea una reserva existente
        Reservation updatedReservation = new Reservation();   // Crea una reserva actualizada

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(existingReservation));  // Simula la reserva existente
        when(reservationRepository.save(any(Reservation.class))).thenReturn(updatedReservation); // Simula la actualización de la reserva

        Reservation result = reservationService.updateReservation(1L, updatedReservation);

        assertNotNull(result);  // Verifica que el resultado no es nulo
        verify(reservationRepository, times(1)).findById(1L);  // Verifica que se buscó la reserva original
        verify(reservationRepository, times(1)).save(existingReservation);  // Verifica que se guardó la reserva actualizada
    }

    // Test para eliminar una reserva
    @Test
    public void testDeleteReservation() {
        doNothing().when(reservationRepository).deleteById(1L);  // Simula la operación de borrado

        reservationService.deleteReservation(1L);

        verify(reservationRepository, times(1)).deleteById(1L);  // Verifica que el método deleteById fue llamado una vez con el ID correcto
    }
}
