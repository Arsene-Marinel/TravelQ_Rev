package com.travelq.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class SecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testProtectedEndpointWithoutAuth() throws Exception {
        mockMvc.perform(get("/private"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testPublicApiAccess() throws Exception {
        mockMvc.perform(get("/api/flights"))
                .andExpect(status().isOk());
    }
}
