package com.travelq.service;

import com.travelq.domain.service.FlightComparisonService;
import com.travelq.domain.service.FlightService;
import com.travelq.dto.FlightComparisonDto;
import com.travelq.dto.FlightDto;
import com.travelq.exception.FlightComparisonNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class FlightComparisonServiceTestProfileTest {

    @Autowired
    private FlightComparisonService flightComparisonService;

    @Autowired
    private FlightService flightService;

    private FlightDto flight1;
    private FlightDto flight2;

    @BeforeEach
    public void setUp() {
        flight1 = new FlightDto();
        flight1.setOrigin("Paris");
        flight1.setDestination("New York");
        flight1.setDepartureTime(LocalDateTime.now().plusDays(5));
        flight1.setArrivalTime(LocalDateTime.now().plusDays(5).plusHours(8));
        flight1.setPrice(BigDecimal.valueOf(700.0));
        flight1.setStopovers(1);
        flight1 = flightService.addFlight(flight1);

        flight2 = new FlightDto();
        flight2.setOrigin("Paris");
        flight2.setDestination("New York");
        flight2.setDepartureTime(LocalDateTime.now().plusDays(6));
        flight2.setArrivalTime(LocalDateTime.now().plusDays(6).plusHours(7));
        flight2.setPrice(BigDecimal.valueOf(650.0));
        flight2.setStopovers(0);
        flight2 = flightService.addFlight(flight2);
    }

    @Test
    public void testAddFlightComparison() {
        FlightComparisonDto comparisonDto = new FlightComparisonDto();
        comparisonDto.setFlights(Arrays.asList(flight1, flight2));

        FlightComparisonDto created = flightComparisonService.addFlightComparison(comparisonDto);

        assertNotNull(created.getId());
        assertEquals(2, created.getFlights().size());
        assertTrue(created.getFlights().contains(flight2));
    }

    @Test
    public void testGetAllFlightComparisons() {
        FlightComparisonDto comparisonDto = new FlightComparisonDto();
        comparisonDto.setFlights(Arrays.asList(flight1, flight2));

        flightComparisonService.addFlightComparison(comparisonDto);

        List<FlightComparisonDto> comparisons = flightComparisonService.getAllFlightComparisons();
        assertFalse(comparisons.isEmpty());
    }

    @Test
    public void testGetFlightComparisonById() {
        FlightComparisonDto comparisonDto = new FlightComparisonDto();
        comparisonDto.setFlights(Arrays.asList(flight1, flight2));

        FlightComparisonDto created = flightComparisonService.addFlightComparison(comparisonDto);

        FlightComparisonDto found = flightComparisonService.getFlightComparisonById(created.getId());

        assertEquals(created.getId(), found.getId());
        assertEquals(2, found.getFlights().size());
    }

    @Test
    public void testUpdateFlightComparison() {
        FlightComparisonDto comparisonDto = new FlightComparisonDto();
        comparisonDto.setFlights(Arrays.asList(flight1, flight2));

        FlightComparisonDto created = flightComparisonService.addFlightComparison(comparisonDto);

        FlightComparisonDto updateDto = new FlightComparisonDto();
        updateDto.setFlights(Arrays.asList(flight1, flight2));

        FlightComparisonDto updated = flightComparisonService.updateFlightComparison(created.getId(), updateDto);

        assertEquals(2, updated.getFlights().size());
    }

    @Test
    public void testDeleteFlightComparison() {
        FlightComparisonDto comparisonDto = new FlightComparisonDto();
        comparisonDto.setFlights(Arrays.asList(flight1, flight2));

        FlightComparisonDto created = flightComparisonService.addFlightComparison(comparisonDto);

        flightComparisonService.deleteFlightComparison(created.getId());

        assertThrows(FlightComparisonNotFoundException.class,
                () -> flightComparisonService.getFlightComparisonById(created.getId()));
    }
}
