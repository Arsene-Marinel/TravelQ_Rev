package com.travelq.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.travelq.dto.TravelHistoryDto;
import com.travelq.dto.UserDto;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class JacksonConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper mapper = builder.build();
        
        // Add annotations programmatically to prevent circular references
        mapper.addMixIn(UserDto.class, UserMixIn.class);
        mapper.addMixIn(TravelHistoryDto.class, TravelHistoryMixIn.class);
        
        return mapper;
    }
    
    // MixIn for UserDto to handle circular references
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    public abstract class UserMixIn {
    }
    
    // MixIn for TravelHistoryDto to handle circular references
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    public abstract class TravelHistoryMixIn {
    }
} 