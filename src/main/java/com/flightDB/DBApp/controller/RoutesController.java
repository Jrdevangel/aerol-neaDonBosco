// src/main/java/com/flightDB/DBApp/controller/RoutesController.java

package com.flightDB.DBApp.controller;

import com.flightDB.DBApp.model.Routes;
import com.flightDB.DBApp.service.RoutesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
@CrossOrigin(origins = "*")
public class RoutesController {

    private final RoutesService routesService;

    @Autowired
    public RoutesController(RoutesService routesService) {
        this.routesService = routesService;
    }


    @PostMapping(path = "/new/routes")
    public ResponseEntity<Routes> createRoute(@RequestBody Routes route) {
        Routes created = routesService.createRoute(route);
        return ResponseEntity.ok(created);
    }


    @GetMapping(path = "/routes/{id}")
    public ResponseEntity<Routes> getRouteById(@PathVariable Long id) {
        Optional<Routes> routeOpt = routesService.getRouteById(id);
        if (routeOpt.isPresent()) {
            return ResponseEntity.ok(routeOpt.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping(path = "/routes")
    public ResponseEntity<List<Routes>> getAllRoutes() {
        List<Routes> routes = routesService.getAllRoutes();
        return ResponseEntity.ok(routes);
    }


    @PutMapping(path = "/update/routes/{id}")
    public ResponseEntity<Routes> updateRoute(@PathVariable Long id, @RequestBody Routes route) {
        Routes updated = routesService.updateRoute(id, route);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.notFound().build();
        }
    }


    @DeleteMapping(path = "/delete/routes/{id}")
    public ResponseEntity<Void> deleteRoute(@PathVariable Long id) {
        routesService.deleteRoute(id);
        return ResponseEntity.noContent().build();
    }
}
