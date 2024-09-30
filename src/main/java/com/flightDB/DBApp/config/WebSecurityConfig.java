package com.flightDB.DBApp.config;

import com.flightDB.DBApp.jwt.AuthTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final AuthTokenFilter authTokenFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Deshabilitar CSRF para APIs RESTful que utilizan tokens
                .csrf(csrf -> csrf.disable())

                // Configuración de autorización de solicitudes
                .authorizeHttpRequests(auth -> auth
                        // Rutas públicas
                        .requestMatchers(
                                "/api/auth/**",
                                "/api/flight/search",
                                "/api/test/all"
                        ).permitAll()

                        // Rutas accesibles para usuarios con rol ADMIN
                        .requestMatchers(
                                "/api/flight/**",
                                "/api/v1/new/routes",
                                "/api/v1/update/routes/{id}",
                                "/api/v1/delete/routes/{id}"
                        ).hasRole("ADMIN")

                        // Rutas accesibles para usuarios con autoridad ADMIN o USER
                        .requestMatchers(
                                "/api/test/user",
                                "/api/test/admin",
                                "/api/v1/routes/**",
                                "/api/v1/new/reservation",
                                "/api/v1/update/reservation/{id}",
                                "/api/v1/delete/reservation/{id}",
                                "/api/v1/reservation/**",
                                "/api/v1/new/passengers",
                                "/api/v1/update/passengers/{id}",
                                "/api/v1/delete/passengers/{id}",
                                "/api/v1/delete/flight/{id}",
                                "/api/v1/update/flight/{id}"
                        ).hasAnyAuthority("ROLE_ADMIN", "ROLE_USER") // Asegúrate de que las autoridades tengan el prefijo "ROLE_"

                        // Rutas permitidas para cualquier usuario autenticado
                        .requestMatchers(
                                "/api/v1/passengers/**"
                        ).hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")

                        // Cualquier otra solicitud requiere autenticación
                        .anyRequest().authenticated()
                )

                // Configuración de gestión de sesiones sin estado (JWT)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Configuración del proveedor de autenticación
                .authenticationProvider(authenticationProvider)

                // Añadir el filtro JWT antes del filtro de autenticación de usuario y contraseña
                .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
