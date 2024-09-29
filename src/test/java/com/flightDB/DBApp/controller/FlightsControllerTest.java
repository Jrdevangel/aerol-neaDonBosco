package com.flightDB.DBApp.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.flightDB.DBApp.model.Flight;
import com.flightDB.DBApp.model.Passengers;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import com.flightDB.DBApp.model.Routes;
import com.flightDB.DBApp.service.FlightsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import java.time.LocalDate;
import java.util.ArrayList;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;
class FlightsControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FlightsService flightsService;

    @InjectMocks
    private FlightsController flightsController;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(flightsController).build();
        objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
    }

    @Test
    void getAllFlights() throws Exception {
        Routes origin = new Routes(1L, "Spain", "Madrid");
        Routes destination = new Routes(2L, "France", "Paris");
        Passengers passengers = new Passengers(1L, 200, 100);
        Flight flight = new Flight(1L, null, destination, origin, passengers);
        ArrayList<Flight> flightList = new ArrayList<>();
        flightList.add(flight);

        when(flightsService.getAllFlight()).thenReturn(flightList);
        mockMvc.perform(get("/api/flight/get")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));

        verify(flightsService, times(1)).getAllFlight();
    }

        @Test
    void getAllFlightBySearch() throws Exception{
            LocalDate testDate = LocalDate.of(2024, 9, 27);
            Routes origin = new Routes(1L, "Spain", "Madrid");
            Routes destination = new Routes(2L, "France", "Paris");
            Passengers passengers = new Passengers(1L, 200, 100);
            Flight flight = new Flight(1L, testDate, destination, origin, passengers);
            ArrayList<Flight> flightList = new ArrayList<>();
            flightList.add(flight);
            when(flightsService.getAllFlightBySearch(anyString(), anyString(), anyString(), anyString(), any(LocalDate.class)))
                    .thenReturn(flightList);
            mockMvc.perform(get("/api/flight/search")
                            .param("originCountry", "Spain")
                            .param("originCity", "Madrid")
                            .param("destinationCountry", "France")
                            .param("destinationCity", "Paris")
                            .param("localDate", testDate.toString())
                            .accept(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$").isArray())
                    .andExpect(jsonPath("$.length()").value(1));
        }

    @Test
    void deleteFlight() throws Exception{
        mockMvc.perform(delete("/api/flight/delete/1"))
                .andExpect(status().isOk());

        verify(flightsService, times(1)).deleteFright(1L);
    }

    @Test
    void updateFlight() throws Exception {
        Long flightId = 1L;
        LocalDate testDate = LocalDate.of(2024, 9, 27);
        Routes origin = new Routes(1L, "Spain", "Madrid");
        Routes destination = new Routes(2L, "France", "Paris");
        Passengers passengers = new Passengers(1L, 200, 100);
        Flight newFlight = new Flight(flightId, testDate, destination, origin, passengers);
        when(flightsService.updateFlight(eq(flightId), any(Flight.class))).thenReturn(newFlight);
        mockMvc.perform(put("/api/flight/update/{id}", flightId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newFlight)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(flightId));
        verify(flightsService, times(1)).updateFlight(eq(flightId), any(Flight.class));
    }

    @Test
    void createFlight() throws Exception {
        LocalDate departureDate = LocalDate.of(2024, 9, 27);
        Routes origin = new Routes(1L, "Spain", "Madrid");
        Routes destination = new Routes(2L, "France", "Paris");
        Passengers passengers = new Passengers(1L, 200, 100);
        Flight newFlight = new Flight(null, departureDate, destination, origin, passengers);
        Flight createdFlight = new Flight(1L, departureDate, destination, origin, passengers);

        when(flightsService.createFlight(any(Flight.class))).thenReturn(createdFlight);

        mockMvc.perform(post("/api/flight/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newFlight)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
        verify(flightsService, times(1)).createFlight(any(Flight.class));
    }
}