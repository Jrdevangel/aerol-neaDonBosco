package com.flightDB.DBApp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class InsertRoutesService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired private SeatsService seatsService;

    public void insertInitialRoutes() {
        String checkQuery = "SELECT COUNT(*) FROM routes WHERE city = ? AND country = ?";
        String insertQuery = "INSERT INTO routes (city, country) VALUES (?, ?)";

        Object[][] routesData = {
                {"Seville", "Portugal"},
                {"Berlin", "Germany"},
                {"Florence", "Italy"}
        };

        for (Object[] route : routesData) {
            Integer count = jdbcTemplate.queryForObject(checkQuery, Integer.class, route);
            if (count != null && count == 0) {
                jdbcTemplate.update(insertQuery, route);
            }
        }

        System.out.println("Вставки завершено успішно!");
    }
}
