package com.travelq.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelq.domain.model.FlightEntity;
import com.travelq.domain.model.TicketEntity;
import com.travelq.domain.model.UserEntity;
import com.travelq.domain.repository.FlightRepository;
import com.travelq.domain.repository.TicketRepository;
import com.travelq.domain.repository.TravelOptionRepository;
import com.travelq.domain.repository.UserRepository;
import com.travelq.dto.TravelOptionDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TravelOptionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TravelOptionRepository travelOptionRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Long ticketId;

    @BeforeEach
    void setup() {
        travelOptionRepository.deleteAll();
        ticketRepository.deleteAll();
        flightRepository.deleteAll();
        userRepository.deleteAll();

        // Create base User + Flight + Ticket
        UserEntity user = new UserEntity();
        user.setUsername("testuser");
        user.setPassword("password123");
        user.setEmail("test@example.com");
        user.setFirstName("Marinel");
        user.setLastName("Arsene");
        user = userRepository.save(user);

        FlightEntity flight = new FlightEntity();
        flight.setOrigin("Paris");
        flight.setDestination("Rome");
        flight.setDepartureTime(LocalDateTime.now().plusDays(1));
        flight.setArrivalTime(LocalDateTime.now().plusDays(1).plusHours(2));
        flight.setPrice(java.math.BigDecimal.valueOf(150));
        flight = flightRepository.save(flight);

        TicketEntity ticket = new TicketEntity();
        ticket.setUser(user);
        ticket.setFlight(flight);
        ticket.setPurchaseDate(LocalDateTime.now());
        ticket = ticketRepository.save(ticket);

        ticketId = ticket.getId();
    }

    @Test
    void testCreateTravelOption() throws Exception {
        TravelOptionDto travelOptionDto = new TravelOptionDto(null, true, "BUSINESS", "CHECKED_BAGGAGE", ticketId);

        mockMvc.perform(post("/api/traveloptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(travelOptionDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.seatSelection", is("BUSINESS")))
                .andExpect(jsonPath("$.checkIn", is(true)))
                .andExpect(jsonPath("$.extraBaggage", is("CHECKED_BAGGAGE")));
    }

    @Test
    void testGetAllTravelOptions() throws Exception {
        testCreateTravelOption();

        mockMvc.perform(get("/api/traveloptions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testGetTravelOptionById() throws Exception {
        String response = mockMvc.perform(post("/api/traveloptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new TravelOptionDto(null, false, "ECONOMY", "OVERWEIGHT_BAGGAGE", ticketId))))
                .andReturn().getResponse().getContentAsString();

        TravelOptionDto created = objectMapper.readValue(response, TravelOptionDto.class);

        mockMvc.perform(get("/api/traveloptions/" + created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(created.getId().intValue())))
                .andExpect(jsonPath("$.seatSelection", is("ECONOMY")));
    }

    @Test
    void testUpdateTravelOption() throws Exception {
        String response = mockMvc.perform(post("/api/traveloptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new TravelOptionDto(null, true, "BUSINESS", "CARRIED_BAGGAGE", ticketId))))
                .andReturn().getResponse().getContentAsString();

        TravelOptionDto created = objectMapper.readValue(response, TravelOptionDto.class);
        created.setSeatSelection("ECONOMY");
        created.setExtraBaggage("SPECIAL_BAGGAGE");
        created.setCheckIn(false);

        mockMvc.perform(put("/api/traveloptions/" + created.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(created)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.seatSelection", is("ECONOMY")))
                .andExpect(jsonPath("$.checkIn", is(false)))
                .andExpect(jsonPath("$.extraBaggage", is("SPECIAL_BAGGAGE")));
    }

    @Test
    void testDeleteTravelOption() throws Exception {
        String response = mockMvc.perform(post("/api/traveloptions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new TravelOptionDto(null, true, "ECONOMY", "CARRIED_BAGGAGE", ticketId))))
                .andReturn().getResponse().getContentAsString();

        TravelOptionDto created = objectMapper.readValue(response, TravelOptionDto.class);

        mockMvc.perform(delete("/api/traveloptions/" + created.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));
    }
}

