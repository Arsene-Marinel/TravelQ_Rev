package com.travelq.service;

import com.travelq.domain.service.TicketService;
import com.travelq.domain.service.UserService;
import com.travelq.domain.service.FlightService;
import com.travelq.dto.FlightDto;
import com.travelq.dto.TicketDto;
import com.travelq.dto.UserDto;
import com.travelq.exception.TicketNotFoundException;
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
public class TicketServiceTestProfileTest {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserService userService;

    @Autowired
    private FlightService flightService;

    private UserDto user;
    private FlightDto flight;

    @BeforeEach
    public void setUp() {
        user = new UserDto();
        user.setUsername("ticket_user");
        user.setPassword("pass123");
        user.setEmail("ticket_user@example.com");
        user.setFirstName("Tic");
        user.setLastName("Ket");
        user = userService.addUser(user);

        flight = new FlightDto();
        flight.setOrigin("Bucharest");
        flight.setDestination("London");
        flight.setDepartureTime(LocalDateTime.now().plusDays(1));
        flight.setArrivalTime(LocalDateTime.now().plusDays(1).plusHours(3));
        flight.setPrice(BigDecimal.valueOf(150.0));
        flight.setStopovers(0);
        flight = flightService.addFlight(flight);
    }

    @Test
    public void testAddTicket() {
        TicketDto ticket = new TicketDto();
        ticket.setUser(user);
        ticket.setFlight(flight);
        ticket.setPurchaseDate(LocalDateTime.now());

        TicketDto created = ticketService.addTicket(ticket);

        assertNotNull(created.getId());
        assertEquals(user.getId(), created.getUser().getId());
        assertEquals(flight.getId(), created.getFlight().getId());
    }

    @Test
    public void testGetAllTickets() {
        TicketDto ticket = new TicketDto();
        ticket.setUser(user);
        ticket.setFlight(flight);
        ticket.setPurchaseDate(LocalDateTime.now());

        ticketService.addTicket(ticket);

        List<TicketDto> tickets = ticketService.getAllTickets();
        assertFalse(tickets.isEmpty());
    }

    @Test
    public void testGetTicketById() {
        TicketDto ticket = new TicketDto();
        ticket.setUser(user);
        ticket.setFlight(flight);
        ticket.setPurchaseDate(LocalDateTime.now());

        TicketDto created = ticketService.addTicket(ticket);
        TicketDto found = ticketService.getTicketById(created.getId());

        assertEquals(created.getId(), found.getId());
        assertEquals(user.getId(), found.getUser().getId());
        assertEquals(flight.getId(), found.getFlight().getId());
    }

    @Test
    public void testUpdateTicket() {
        TicketDto ticket = new TicketDto();
        ticket.setUser(user);
        ticket.setFlight(flight);
        ticket.setPurchaseDate(LocalDateTime.now());

        TicketDto created = ticketService.addTicket(ticket);

        TicketDto update = new TicketDto();
        update.setUser(user);
        update.setFlight(flight);
        update.setPurchaseDate(LocalDateTime.now().plusDays(2));

        TicketDto updated = ticketService.updateTicket(created.getId(), update);

        assertEquals(update.getPurchaseDate(), updated.getPurchaseDate());
    }

    @Test
    public void testDeleteTicket() {
        TicketDto ticket = new TicketDto();
        ticket.setUser(user);
        ticket.setFlight(flight);
        ticket.setPurchaseDate(LocalDateTime.now());

        TicketDto created = ticketService.addTicket(ticket);
        ticketService.deleteTicket(created.getId());

        assertThrows(TicketNotFoundException.class, () -> ticketService.getTicketById(created.getId()));
    }
}
