package com.flightDB.DBApp;

import com.flightDB.DBApp.service.InsertRoutesService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DbAppApplication {

	public static void main(String[] args) {
		SpringApplication.run(DbAppApplication.class, args);
	}

	// Ejecutar el servicio de inserción de datos al iniciar la aplicación
	@Bean
	CommandLineRunner run(InsertRoutesService insertRoutesService) {
		return args -> {
			insertRoutesService.insertInitialRoutes();
		};
	}
}
