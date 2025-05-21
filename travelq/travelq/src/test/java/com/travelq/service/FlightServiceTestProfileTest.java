package com.travelq.service;

import com.travelq.domain.service.FlightService;
import com.travelq.dto.FlightDto;
import com.travelq.exception.FlightNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class FlightServiceTestProfileTest {

    @Autowired
    private FlightService flightService;

    private FlightDto baseFlight;

    @BeforeEach
    public void setUp() {
        baseFlight = new FlightDto();
        baseFlight.setOrigin("New York");
        baseFlight.setDestination("Paris");
        baseFlight.setDepartureTime(LocalDateTime.now().plusDays(1));
        baseFlight.setArrivalTime(LocalDateTime.now().plusDays(1).plusHours(7));
        baseFlight.setPrice(BigDecimal.valueOf(500.0));
        baseFlight.setStopovers(1);
    }

    @Test
    public void testAddFlight() {
        FlightDto created = flightService.addFlight(baseFlight);

        assertNotNull(created.getId());
        assertEquals(baseFlight.getOrigin(), created.getOrigin());
        assertEquals(baseFlight.getDestination(), created.getDestination());
    }

    @Test
    public void testGetAllFlights() {
        flightService.addFlight(baseFlight);
        List<FlightDto> flights = flightService.getAllFlights();
        assertFalse(flights.isEmpty());
    }

    @Test
    public void testGetFlightById() {
        FlightDto created = flightService.addFlight(baseFlight);
        FlightDto found = flightService.getFlightById(created.getId());

        assertEquals(created.getId(), found.getId());
        assertEquals(created.getOrigin(), found.getOrigin());
    }

    @Test
    public void testUpdateFlight() {
        FlightDto created = flightService.addFlight(baseFlight);

        FlightDto update = new FlightDto();
        update.setOrigin("Los Angeles");
        update.setDestination("Tokyo");
        update.setDepartureTime(LocalDateTime.now().plusDays(3));
        update.setArrivalTime(LocalDateTime.now().plusDays(3).plusHours(11));
        update.setPrice(BigDecimal.valueOf(800.0));
        update.setStopovers(0);

        FlightDto updated = flightService.updateFlight(created.getId(), update);

        assertEquals("Los Angeles", updated.getOrigin());
        assertEquals("Tokyo", updated.getDestination());
        assertEquals(BigDecimal.valueOf(800.0), updated.getPrice());
    }

    @Test
    public void testDeleteFlight() {
        FlightDto created = flightService.addFlight(baseFlight);
        flightService.deleteFlight(created.getId());

        assertThrows(FlightNotFoundException.class, () -> flightService.getFlightById(created.getId()));
    }
}
