package com.travelq.controller;

import com.travelq.dto.TicketDto;
import com.travelq.dto.StandardResponse;
import com.travelq.domain.service.TicketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tickets")
@Slf4j
public class TicketController {

    private final TicketService ticketService;

    @PostMapping
    public ResponseEntity<TicketDto> createTicket(@Valid @RequestBody TicketDto ticketDto) {
        log.info("Creating new ticket");
        TicketDto created = ticketService.addTicket(ticketDto);
        log.info("Ticket created with ID: {}", created.getId());
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<TicketDto>> getAllTickets() {
        log.info("Fetching all tickets");
        List<TicketDto> tickets = ticketService.getAllTickets();
        log.info("Retrieved {} tickets", tickets.size());
        return ResponseEntity.ok(tickets);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketDto> getTicketById(@PathVariable Long id) {
        log.info("Fetching ticket with ID: {}", id);
        TicketDto ticket = ticketService.getTicketById(id);
        log.info("Ticket found with ID: {}", id);
        return ResponseEntity.ok(ticket);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketDto> updateTicket(@PathVariable Long id, @Valid @RequestBody TicketDto ticketDto) {
        log.info("Updating ticket with ID: {}", id);
        TicketDto updated = ticketService.updateTicket(id, ticketDto);
        log.info("Ticket updated with ID: {}", id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StandardResponse> deleteTicket(@PathVariable Long id) {
        log.info("Deleting ticket with ID: {}", id);
        ticketService.deleteTicket(id);
        log.info("Ticket deleted with ID: {}", id);
        return ResponseEntity.ok(StandardResponse.success("Ticket with ID " + id + " was deleted."));
    }
}
