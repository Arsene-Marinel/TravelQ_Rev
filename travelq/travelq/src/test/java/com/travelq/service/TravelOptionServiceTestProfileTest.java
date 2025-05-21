package com.travelq.service;

import com.travelq.domain.service.TravelOptionService;
import com.travelq.domain.service.UserService;
import com.travelq.domain.service.FlightService;
import com.travelq.domain.service.TicketService;
import com.travelq.dto.*;
import com.travelq.exception.TravelOptionNotFoundException;
import com.travelq.domain.enums.TravelOptionExtraBaggageEnum;
import com.travelq.domain.enums.TravelOptionSeatSelectionEnum;
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
public class TravelOptionServiceTestProfileTest {

    @Autowired
    private TravelOptionService travelOptionService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserService userService;

    @Autowired
    private FlightService flightService;

    private TicketDto ticket;

    @BeforeEach
    public void setUp() {
        UserDto user = new UserDto();
        user.setUsername("travel_option_user");
        user.setPassword("password");
        user.setEmail("option@example.com");
        user.setFirstName("Travel");
        user.setLastName("Option");
        user = userService.addUser(user);

        FlightDto flight = new FlightDto();
        flight.setOrigin("Paris");
        flight.setDestination("Rome");
        flight.setDepartureTime(LocalDateTime.now().plusDays(1));
        flight.setArrivalTime(LocalDateTime.now().plusDays(1).plusHours(2));
        flight.setPrice(BigDecimal.valueOf(99.99));
        flight.setStopovers(0);
        flight = flightService.addFlight(flight);

        ticket = new TicketDto();
        ticket.setUser(user);
        ticket.setFlight(flight);
        ticket.setPurchaseDate(LocalDateTime.now());
        ticket = ticketService.addTicket(ticket);
    }

    @Test
    public void testAddTravelOption() {
        TravelOptionDto dto = new TravelOptionDto();
        dto.setCheckIn(true);
        dto.setSeatSelection(TravelOptionSeatSelectionEnum.ECONOMY.toString());
        dto.setExtraBaggage(TravelOptionExtraBaggageEnum.CHECKED_BAGGAGE.toString());
        dto.setTicket(ticket);

        TravelOptionDto created = travelOptionService.addTravelOption(dto);

        assertNotNull(created.getId());
        assertTrue(created.isCheckIn());
        assertEquals(TravelOptionSeatSelectionEnum.ECONOMY.toString(), created.getSeatSelection());
    }

    @Test
    public void testGetAllTravelOptions() {
        TravelOptionDto dto = new TravelOptionDto();
        dto.setCheckIn(false);
        dto.setSeatSelection(TravelOptionSeatSelectionEnum.BUSINESS.toString());
        dto.setExtraBaggage(TravelOptionExtraBaggageEnum.CARRIED_BAGGAGE.toString());
        dto.setTicket(ticket);

        travelOptionService.addTravelOption(dto);

        List<TravelOptionDto> all = travelOptionService.getAllTravelOptions();
        assertFalse(all.isEmpty());
    }

    @Test
    public void testGetTravelOptionById() {
        TravelOptionDto dto = new TravelOptionDto();
        dto.setCheckIn(true);
        dto.setSeatSelection(TravelOptionSeatSelectionEnum.ECONOMY.toString());
        dto.setExtraBaggage(TravelOptionExtraBaggageEnum.SPECIAL_BAGGAGE.toString());
        dto.setTicket(ticket);

        TravelOptionDto created = travelOptionService.addTravelOption(dto);
        TravelOptionDto found = travelOptionService.getTravelOptionById(created.getId());

        assertEquals(created.getSeatSelection(), found.getSeatSelection());
        assertEquals(created.getExtraBaggage(), found.getExtraBaggage());
    }

    @Test
    public void testUpdateTravelOption() {
        TravelOptionDto dto = new TravelOptionDto();
        dto.setCheckIn(false);
        dto.setSeatSelection(TravelOptionSeatSelectionEnum.ECONOMY.toString());
        dto.setExtraBaggage(TravelOptionExtraBaggageEnum.CARRIED_BAGGAGE.toString());
        dto.setTicket(ticket);

        TravelOptionDto created = travelOptionService.addTravelOption(dto);

        TravelOptionDto update = new TravelOptionDto();
        update.setCheckIn(true);
        update.setSeatSelection(TravelOptionSeatSelectionEnum.BUSINESS.toString());
        update.setExtraBaggage(TravelOptionExtraBaggageEnum.OVERWEIGHT_BAGGAGE.toString());
        update.setTicket(ticket);

        TravelOptionDto updated = travelOptionService.updateTravelOption(created.getId(), update);

        assertTrue(updated.isCheckIn());
        assertEquals(TravelOptionSeatSelectionEnum.BUSINESS.toString(), updated.getSeatSelection());
        assertEquals(TravelOptionExtraBaggageEnum.OVERWEIGHT_BAGGAGE.toString(), updated.getExtraBaggage());
    }

    @Test
    public void testDeleteTravelOption() {
        TravelOptionDto dto = new TravelOptionDto();
        dto.setCheckIn(true);
        dto.setSeatSelection(TravelOptionSeatSelectionEnum.BUSINESS.toString());
        dto.setExtraBaggage(TravelOptionExtraBaggageEnum.OVERSIDE_BAGGAGE.toString());
        dto.setTicket(ticket);

        TravelOptionDto created = travelOptionService.addTravelOption(dto);
        travelOptionService.deleteTravelOption(created.getId());

        assertThrows(TravelOptionNotFoundException.class, () -> travelOptionService.getTravelOptionById(created.getId()));
    }
}
