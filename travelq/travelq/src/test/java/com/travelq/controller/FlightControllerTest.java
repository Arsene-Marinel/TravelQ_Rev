package com.travelq.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelq.domain.repository.FlightRepository;
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

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class FlightControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private FlightDto sampleFlight;

    @BeforeEach
    void setup() {
        flightRepository.deleteAll();

        sampleFlight = new FlightDto();
        sampleFlight.setOrigin("Cluj");
        sampleFlight.setDestination("Berlin");
        sampleFlight.setDepartureTime(LocalDateTime.now().plusDays(2));
        sampleFlight.setArrivalTime(LocalDateTime.now().plusDays(2).plusHours(3));
        sampleFlight.setPrice(new BigDecimal("199.99"));
        sampleFlight.setStopovers(1);
    }

    @Test
    void testCreateFlight() throws Exception {
        mockMvc.perform(post("/api/flights")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleFlight)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.origin", is("Cluj")))
                .andExpect(jsonPath("$.destination", is("Berlin")));
    }

    @Test
    void testGetAllFlights() throws Exception {
        testCreateFlight();

        mockMvc.perform(get("/api/flights"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testGetFlightById() throws Exception {
        String response = mockMvc.perform(post("/api/flights")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleFlight)))
                .andReturn().getResponse().getContentAsString();

        FlightDto createdFlight = objectMapper.readValue(response, FlightDto.class);

        mockMvc.perform(get("/api/flights/" + createdFlight.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(createdFlight.getId().intValue())));
    }

    @Test
    void testUpdateFlight() throws Exception {
        String response = mockMvc.perform(post("/api/flights")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleFlight)))
                .andReturn().getResponse().getContentAsString();

        FlightDto createdFlight = objectMapper.readValue(response, FlightDto.class);
        createdFlight.setStopovers(2);
        createdFlight.setDestination("Munich");

        mockMvc.perform(put("/api/flights/" + createdFlight.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createdFlight)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.destination", is("Munich")))
                .andExpect(jsonPath("$.stopovers", is(2)));
    }

    @Test
    void testDeleteFlight() throws Exception {
        String response = mockMvc.perform(post("/api/flights")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleFlight)))
                .andReturn().getResponse().getContentAsString();

        FlightDto createdFlight = objectMapper.readValue(response, FlightDto.class);

        mockMvc.perform(delete("/api/flights/" + createdFlight.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));

        mockMvc.perform(get("/api/flights/" + createdFlight.getId()))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void testGetAllFlightsPaginated() throws Exception {
        for (int i = 0; i < 5; i++) {
            FlightDto flight = new FlightDto();
            flight.setOrigin("City" + i);
            flight.setDestination("Destination" + i);
            flight.setDepartureTime(LocalDateTime.now().plusDays(i + 1));
            flight.setArrivalTime(LocalDateTime.now().plusDays(i + 1).plusHours(2));
            flight.setPrice(new BigDecimal(100 + i * 10));
            flight.setStopovers(i % 2);
            mockMvc.perform(post("/api/flights")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(flight)))
                    .andExpect(status().isOk());
        }

        mockMvc.perform(get("/api/flights/paged")
                        .param("page", "0")
                        .param("size", "3")
                        .param("sortBy", "price")
                        .param("direction", "desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(3)))
                .andExpect(jsonPath("$.content[0].price", greaterThanOrEqualTo(100.0)));
    }
}

