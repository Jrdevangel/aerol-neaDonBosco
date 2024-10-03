package com.flightDB.DBApp.service;

import com.flightDB.DBApp.model.Routes;
import com.flightDB.DBApp.repository.IRoutesRepository;
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

class RouteServiceTest {

    @Mock
    private IRoutesRepository routesRepository;

    @InjectMocks
    private RoutesService routesService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test para crear una ruta
    @Test
    public void testCreateRoute() {
        Routes route = new Routes(1L, "USA", "New York");
        when(routesRepository.save(any(Routes.class))).thenReturn(route);

        Routes createdRoute = routesService.createRoute(route);

        assertNotNull(createdRoute);
        assertEquals("USA", createdRoute.getCountry());
        assertEquals("New York", createdRoute.getCity());
        verify(routesRepository, times(1)).save(route);
    }

    // Test para obtener una ruta por ID
    @Test
    public void testGetRouteById() {
        Routes route = new Routes(1L, "USA", "New York");
        when(routesRepository.findById(1L)).thenReturn(Optional.of(route));

        Optional<Routes> foundRoute = routesService.getRouteById(1L);

        assertTrue(foundRoute.isPresent());
        assertEquals(1L, foundRoute.get().getId());
        verify(routesRepository, times(1)).findById(1L);
    }

    // Test para obtener todas las rutas
    @Test
    public void testGetAllRoutes() {
        Routes route1 = new Routes(1L, "USA", "New York");
        Routes route2 = new Routes(2L, "Canada", "Toronto");
        List<Routes> routesList = new ArrayList<>(List.of(route1, route2));

        when(routesRepository.findAll()).thenReturn(routesList);

        List<Routes> foundRoutes = routesService.getAllRoutes();

        assertEquals(2, foundRoutes.size());
        verify(routesRepository, times(1)).findAll();
    }

    // Test para actualizar una ruta
    @Test
    public void testUpdateRoute() {
        Routes existingRoute = new Routes(1L, "USA", "New York");
        Routes updatedRoute = new Routes(1L, "Canada", "Toronto");

        when(routesRepository.findById(1L)).thenReturn(Optional.of(existingRoute));
        when(routesRepository.save(any(Routes.class))).thenReturn(updatedRoute);

        Routes result = routesService.updateRoute(1L, updatedRoute);

        assertNotNull(result);
        assertEquals("Canada", result.getCountry());
        assertEquals("Toronto", result.getCity());
        verify(routesRepository, times(1)).findById(1L);
        verify(routesRepository, times(1)).save(existingRoute);
    }

    // Test para eliminar una ruta
    @Test
    public void testDeleteRoute() {
        doNothing().when(routesRepository).deleteById(1L);

        routesService.deleteRoute(1L);

        verify(routesRepository, times(1)).deleteById(1L);
    }
}
