package com.travelq.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelq.domain.repository.FlightComparisonRepository;
import com.travelq.domain.repository.FlightRepository;
import com.travelq.dto.FlightComparisonDto;
import com.travelq.dto.FlightDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collections;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class FlightComparisonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private FlightComparisonRepository flightComparisonRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private FlightDto sampleFlight;

    @BeforeEach
    void setup() {
        flightComparisonRepository.deleteAll();
        flightRepository.deleteAll();

        sampleFlight = new FlightDto();
        sampleFlight.setOrigin("Bucharest");
        sampleFlight.setDestination("Paris");
        sampleFlight.setDepartureTime(LocalDateTime.now().plusDays(1));
        sampleFlight.setArrivalTime(LocalDateTime.now().plusDays(1).plusHours(3));
        sampleFlight.setPrice(new BigDecimal("300.00"));
        sampleFlight.setStopovers(0);
    }

    private FlightDto createFlight() throws Exception {
        String response = mockMvc.perform(post("/api/flights")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleFlight)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(response, FlightDto.class);
    }

    private FlightComparisonDto createComparisonWithFlight(FlightDto flight) throws Exception {
        FlightComparisonDto comparisonDto = new FlightComparisonDto();
        comparisonDto.setFlights(Collections.singletonList(flight));

        String response = mockMvc.perform(post("/api/flightcomparisons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comparisonDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        return objectMapper.readValue(response, FlightComparisonDto.class);
    }

    @Test
    void testCreateFlightComparison() throws Exception {
        FlightDto flight = createFlight();
        FlightComparisonDto comparisonDto = new FlightComparisonDto(null, Collections.singletonList(flight));

        mockMvc.perform(post("/api/flightcomparisons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comparisonDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.flights", hasSize(1)))
                .andExpect(jsonPath("$.flights[0].origin", is("Bucharest")));
    }

    @Test
    void testGetAllFlightComparisons() throws Exception {
        FlightDto flight = createFlight();
        createComparisonWithFlight(flight);

        mockMvc.perform(get("/api/flightcomparisons"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testGetFlightComparisonById() throws Exception {
        FlightDto flight = createFlight();
        FlightComparisonDto created = createComparisonWithFlight(flight);

        mockMvc.perform(get("/api/flightcomparisons/" + created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(created.getId().intValue())));
    }

    @Test
    void testDeleteFlightComparison() throws Exception {
        FlightDto flight = createFlight();
        FlightComparisonDto created = createComparisonWithFlight(flight);

        mockMvc.perform(delete("/api/flightcomparisons/" + created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));

        mockMvc.perform(get("/api/flightcomparisons/" + created.getId()))
                .andExpect(status().is4xxClientError());
    }
}

