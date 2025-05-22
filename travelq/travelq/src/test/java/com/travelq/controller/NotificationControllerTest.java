package com.travelq.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelq.dto.NotificationDto;
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
public class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long userId;

    @BeforeEach
    void setUp() throws Exception {
        // Creează un user pentru a atașa notificările
        UserDto userDto = new UserDto();
        userDto.setUsername("notif_user");
        userDto.setPassword("password123");
        userDto.setEmail("notif_user@example.com");
        userDto.setFirstName("Notif");
        userDto.setLastName("User");

        String userResponse = mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        UserDto createdUser = objectMapper.readValue(userResponse, UserDto.class);
        userId = createdUser.getId();
    }

    private NotificationDto createNotificationDto(String message) {
        NotificationDto dto = new NotificationDto();
        dto.setMessage(message);
        dto.setRead(false);
        dto.setUserId(userId);
        return dto;
    }

    @Test
    void testGetAllNotifications() throws Exception {
        NotificationDto dto = createNotificationDto("List test");

        mockMvc.perform(post("/api/notifications")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/notifications"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())));
    }
}

