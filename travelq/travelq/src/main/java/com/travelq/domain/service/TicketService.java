package com.travelq.domain.service;

import com.travelq.dto.TicketDto;

import java.util.List;

public interface TicketService {

    TicketDto addTicket(TicketDto ticketDto);

    List<TicketDto> getAllTickets();

    TicketDto getTicketById(Long ticketId);

    TicketDto updateTicket(Long ticketId, TicketDto ticketDto);

    void deleteTicket(Long ticketId);
}
