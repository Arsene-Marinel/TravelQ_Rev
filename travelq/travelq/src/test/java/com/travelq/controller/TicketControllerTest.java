package com.travelq.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelq.domain.model.FlightEntity;
import com.travelq.domain.model.UserEntity;
import com.travelq.domain.repository.FlightRepository;
import com.travelq.domain.repository.TicketRepository;
import com.travelq.domain.repository.UserRepository;
import com.travelq.dto.FlightDto;
import com.travelq.dto.TicketDto;
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
public class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Long userId;
    private FlightEntity savedFlight;

    @BeforeEach
    void setUp() {
        ticketRepository.deleteAll();
        flightRepository.deleteAll();
        userRepository.deleteAll();

        UserEntity user = new UserEntity();
        user.setUsername("user_test");
        user.setPassword("pass123");
        user.setEmail("user@test.com");
        user.setFirstName("User");
        user.setLastName("Test");
        user = userRepository.save(user);
        userId = user.getId();

        FlightEntity flight = new FlightEntity();
        flight.setOrigin("Paris");
        flight.setDestination("Berlin");
        flight.setDepartureTime(LocalDateTime.now().plusDays(1));
        flight.setArrivalTime(LocalDateTime.now().plusDays(1).plusHours(2));
        flight.setPrice(BigDecimal.valueOf(200));
        flight.setStopovers(0);
        savedFlight = flightRepository.save(flight);
    }

    @Test
    void testCreateTicket() throws Exception {
        TicketDto ticketDto = new TicketDto(null, LocalDateTime.now(), userId,
                new FlightDto(savedFlight.getId(), "Paris", "Berlin",
                        savedFlight.getDepartureTime(), savedFlight.getArrivalTime(),
                        savedFlight.getPrice(), 0, Collections.emptyList(), Collections.emptyList()),
                null);

        mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticketDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId", is(userId.intValue())))
                .andExpect(jsonPath("$.flight.origin", is("Paris")));
    }

    @Test
    void testGetAllTickets() throws Exception {
        testCreateTicket(); // create one ticket

        mockMvc.perform(get("/api/tickets")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testDeleteTicket() throws Exception {
        TicketDto ticketDto = new TicketDto(null, LocalDateTime.now(), userId,
                new FlightDto(savedFlight.getId(), "Paris", "Berlin",
                        savedFlight.getDepartureTime(), savedFlight.getArrivalTime(),
                        savedFlight.getPrice(), 0, Collections.emptyList(), Collections.emptyList()),
                null);

        String response = mockMvc.perform(post("/api/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticketDto)))
                .andReturn().getResponse().getContentAsString();

        TicketDto created = objectMapper.readValue(response, TicketDto.class);

        mockMvc.perform(delete("/api/tickets/" + created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", containsString("Ticket with ID")));
    }
}
