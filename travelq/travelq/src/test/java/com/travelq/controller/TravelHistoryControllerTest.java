package com.travelq.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelq.dto.TravelHistoryDto;
import com.travelq.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class TravelHistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private UserDto dummyUser;

    @BeforeEach
    void setup() throws Exception {
        // Înregistrăm un user înainte de fiecare test (pentru că TravelHistoryDto are nevoie de user)
        UserDto userDto = new UserDto();
        userDto.setUsername("traveluser_" + System.currentTimeMillis());
        userDto.setPassword("Pass1234");
        userDto.setEmail(userDto.getUsername() + "@mail.com");
        userDto.setFirstName("Marinel");
        userDto.setLastName("Arsene");

        String userJson = mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        dummyUser = objectMapper.readValue(userJson, UserDto.class);
    }

    private TravelHistoryDto createTravelHistoryDto() {
        TravelHistoryDto dto = new TravelHistoryDto();
        dto.setFlightsCount(5);
        dto.setTotalSpent(1234.56);
        dto.setUser(dummyUser);
        return dto;
    }

    @Test
    void testCreateTravelHistory() throws Exception {
        TravelHistoryDto dto = createTravelHistoryDto();

        mockMvc.perform(post("/api/travelhistories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.flightsCount", is(5)))
                .andExpect(jsonPath("$.totalSpent", is(1234.56)));
    }

    @Test
    void testGetAllTravelHistories() throws Exception {
        TravelHistoryDto dto = createTravelHistoryDto();

        mockMvc.perform(post("/api/travelhistories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/travelhistories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())));
    }
}
