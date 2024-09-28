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
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(authRequest ->
                        authRequest
                                .requestMatchers("/api/auth/**").permitAll()
                                .requestMatchers("/api/v1/new/routes").hasAuthority("ADMIN")
                                .requestMatchers("/api/v1/delete/routes/{id}").hasAuthority("ADMIN")
                                .requestMatchers("/api/v1/update/routes/{id}").hasAuthority("ADMIN")
                                .requestMatchers("/api/v1/routes/**").permitAll()
                                .requestMatchers("/api/v1/new/reservation").hasAnyAuthority("ADMIN", "USER")
                                .requestMatchers("/api/v1/delete/reservation/{id}").hasAnyAuthority("ADMIN", "USER")
                                .requestMatchers("/api/v1/update/reservation/{id}").hasAnyAuthority("ADMIN", "USER")
                                .requestMatchers("/api/v1/reservation/**").permitAll()
                                .requestMatchers("/api/v1/new/passengers").hasAnyAuthority("ADMIN", "USER")
                                .requestMatchers("/api/v1/delete/passengers/{id}").hasAnyAuthority("ADMIN", "USER")
                                .requestMatchers("/api/v1/update/passengers/{id}").hasAnyAuthority("ADMIN", "USER")
                                .requestMatchers("/api/v1/passengers").permitAll()
                                .requestMatchers("/api/v1/new/flight").hasAuthority("ADMIN")
                                .requestMatchers("/api/v1/delete/flight/{id}").hasAnyAuthority("ADMIN", "USER")
                                .requestMatchers("/api/v1/update/flight/{id}").hasAnyAuthority("ADMIN", "USER")
                                .requestMatchers("/api/v1/passengers").permitAll()
                                .anyRequest().authenticated()
                )
                .sessionManagement(sessionManager ->
                        sessionManager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(authTokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}