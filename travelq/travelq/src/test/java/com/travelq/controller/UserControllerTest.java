package com.travelq.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelq.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private UserDto createUserDto(String username) {
        UserDto userDto = new UserDto();
        userDto.setUsername(username);
        userDto.setPassword("TestPassword123");
        userDto.setEmail(username + "@example.com");
        userDto.setFirstName("Marinel");
        userDto.setLastName("Arsene");
        return userDto;
    }

    @Test
    void testRegisterUser_shouldReturnCreatedUser() throws Exception {
        UserDto userDto = createUserDto("testuser");

        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.username", is("testuser")))
                .andExpect(jsonPath("$.email", is("testuser@example.com")));
    }

    @Test
    void testGetAllUsers() throws Exception {
        UserDto userDto = createUserDto("listuser");

        // Register a user
        mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andExpect(status().isOk());

        // Fetch all users
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", not(empty())));
    }

    @Test
    void testDeleteUser_shouldReturnSuccessResponse() throws Exception {
        UserDto userDto = createUserDto("deleteuser");

        String response = mockMvc.perform(post("/api/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userDto)))
                .andReturn().getResponse().getContentAsString();

        UserDto createdUser = objectMapper.readValue(response, UserDto.class);

        mockMvc.perform(delete("/api/users/" + createdUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)))
                .andExpect(jsonPath("$.message", containsString("was deleted")));
    }
}
