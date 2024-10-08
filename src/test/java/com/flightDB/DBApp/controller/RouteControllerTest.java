package com.flightDB.DBApp.controller;

import com.flightDB.DBApp.model.Routes;
import com.flightDB.DBApp.service.RoutesService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RouteControllerTest {

    @Mock
    private RoutesService routesService;

    @InjectMocks
    private RoutesController routeController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testCreateRoute() {
        Routes route = new Routes(1L, "USA", "New York");
        when(routesService.createRoute(any(Routes.class))).thenReturn(route);

        ResponseEntity<Routes> response = routeController.createRoute(route);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("USA", response.getBody().getCountry());
        assertEquals("New York", response.getBody().getCity());
        verify(routesService, times(1)).createRoute(route);
    }

    @Test
    public void testGetRouteById() {
        Routes route = new Routes(1L, "USA", "New York");
        when(routesService.getRouteById(1L)).thenReturn(Optional.of(route));

        ResponseEntity<Routes> response = routeController.getRouteById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1L, response.getBody().getId());
        verify(routesService, times(1)).getRouteById(1L);
    }

    @Test
    public void testGetAllRoutes() {
        Routes route1 = new Routes(1L, "USA", "New York");
        Routes route2 = new Routes(2L, "Canada", "Toronto");
        List<Routes> routesList = new ArrayList<>(List.of(route1, route2));

        when(routesService.getAllRoutes()).thenReturn(routesList);

        ResponseEntity<List<Routes>> response = routeController.getAllRoutes();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(routesService, times(1)).getAllRoutes();
    }

    @Test
    public void testUpdateRoute() {
        Routes existingRoute = new Routes(1L, "USA", "New York");
        Routes updatedRoute = new Routes(1L, "Canada", "Toronto");

        when(routesService.updateRoute(eq(1L), any(Routes.class))).thenReturn(updatedRoute);

        ResponseEntity<Routes> response = routeController.updateRoute(1L, updatedRoute);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Canada", response.getBody().getCountry());
        assertEquals("Toronto", response.getBody().getCity());
        verify(routesService, times(1)).updateRoute(eq(1L), any(Routes.class));
    }

    @Test
    public void testDeleteRoute() {
        doNothing().when(routesService).deleteRoute(1L);

        ResponseEntity<Void> response = routeController.deleteRoute(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(routesService, times(1)).deleteRoute(1L);
    }
}
