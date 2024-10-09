package com.flightDB.DBApp.controller;

import com.flightDB.DBApp.model.Flight;
import com.flightDB.DBApp.model.FlightImage;
import com.flightDB.DBApp.service.FlightImageService;
import com.flightDB.DBApp.service.FlightsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class FlightImageControllerTest {

    private MockMvc mockMvc;

    @Mock
    private FlightImageService flightImageService;

    @Mock
    private FlightsService flightService;

    @InjectMocks
    private FlightImageController flightImageController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(flightImageController).build();
    }

    @Test
    void getImagesByFlightId() throws Exception {
        Long flightId = 1L;
        List<FlightImage> mockImages = Arrays.asList(new FlightImage(), new FlightImage());
        when(flightImageService.getImagesByFlightId(flightId)).thenReturn(mockImages);

        mockMvc.perform(get("/api/image/flight/{flightId}", flightId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));

        verify(flightImageService, times(1)).getImagesByFlightId(flightId);
    }

    @Test
    void uploadFlightImage_Success() throws Exception {
        Long flightId = 1L;
        byte[] imageContent = "test image content".getBytes();
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", MediaType.IMAGE_JPEG_VALUE, imageContent);

        when(flightService.getFlightById(flightId)).thenReturn(new Flight());

        mockMvc.perform(multipart("/api/image/create/{flightId}", flightId)
                        .file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("Image uploaded successfully."));

        verify(flightImageService, times(1)).saveFlightImage(any(FlightImage.class));
    }

    @Test
    void uploadFlightImage_EmptyFile() throws Exception {
        Long flightId = 1L;
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", MediaType.IMAGE_JPEG_VALUE, new byte[0]);

        mockMvc.perform(multipart("/api/image/create/{flightId}", flightId)
                        .file(file))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("File cannot be empty."));

        verify(flightImageService, never()).saveFlightImage(any(FlightImage.class));
    }

    @Test
    void uploadFlightImage_FlightNotFound() throws Exception {
        Long flightId = 1L;
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "test content".getBytes());

        when(flightService.getFlightById(flightId)).thenReturn(null);

        mockMvc.perform(multipart("/api/image/create/{flightId}", flightId)
                        .file(file))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Flight not found."));

        verify(flightImageService, never()).saveFlightImage(any(FlightImage.class));
    }

    @Test
    void deleteFlightImage() throws Exception {
        Long imageId = 1L;

        mockMvc.perform(delete("/api/image/delete/{id}", imageId))
                .andExpect(status().isOk());

        verify(flightImageService, times(1)).deleteFlightImage(imageId);
    }

    @Test
    void updateFlightImage_Success() throws Exception {
        Long flightImageId = 1L;
        byte[] imageContent = "updated image content".getBytes();
        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", MediaType.IMAGE_JPEG_VALUE, imageContent);

        when(flightImageService.getById(flightImageId)).thenReturn(new FlightImage());

        mockMvc.perform(multipart("/api/image/update/{flightImageId}", flightImageId)
                        .file(file))
                .andExpect(status().isOk())
                .andExpect(content().string("Image updated successfully."));

        verify(flightImageService, times(1)).saveFlightImage(any(FlightImage.class));
    }

}
