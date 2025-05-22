package com.travelq.config;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.travelq.dto.TravelHistoryDto;
import com.travelq.dto.UserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class JacksonTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testObjectMapperHandlesCircularReferences() {
        // create dummy TravelHistoryDto and UserDto with circular reference
        UserDto user = new UserDto();
        user.setId(1L);
        user.setUsername("jdoe");

        TravelHistoryDto history = new TravelHistoryDto();
        history.setId(10L);
        history.setUser(user);

        user.setTravelHistory(history);

        assertDoesNotThrow(() -> {
            String json = objectMapper.writeValueAsString(user);
            assertNotNull(json);
            assertTrue(json.contains("\"id\":1"));
            assertTrue(json.contains("\"id\":10"));
        });
    }

    @Test
    void testObjectMapperIsConfiguredWithMixIns() {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(TestConfig.class);
        ObjectMapper objectMapper = context.getBean(ObjectMapper.class);

        assertNotNull(objectMapper);

        Class<?> userMixIn = objectMapper.findMixInClassFor(UserDto.class);
        Class<?> travelHistoryMixIn = objectMapper.findMixInClassFor(TravelHistoryDto.class);

        assertNotNull(userMixIn);
        assertTrue(userMixIn.isAnnotationPresent(JsonIdentityInfo.class));

        assertNotNull(travelHistoryMixIn);
        assertTrue(travelHistoryMixIn.isAnnotationPresent(JsonIdentityInfo.class));

        context.close();
    }

    @Configuration
    @Import(JacksonConfig.class)
    static class TestConfig {
        @Bean
        public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder() {
            return new Jackson2ObjectMapperBuilder();
        }
    }
}

