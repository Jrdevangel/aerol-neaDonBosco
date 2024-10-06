package com.flightDB.DBApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class InsertRoutesService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Método que inserta los datos en la tabla `routes`
    public void insertInitialRoutes() {
        // Consulta SQL para insertar datos sin el campo `id`
        String insertQuery = "INSERT INTO routes (city, country) VALUES (?, ?)";

        // Datos a insertar
        Object[][] routesData = {
                {"Xerez", "Spain"},
                {"Madrid", "Spain"},
                {"Paris", "France"}
        };

        // Insertar cada fila de datos
        for (Object[] route : routesData) {
            jdbcTemplate.update(insertQuery, route);
        }

        System.out.println("Inserciones completadas exitosamente en la tabla routes!");
    }
}
