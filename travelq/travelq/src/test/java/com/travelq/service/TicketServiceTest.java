package com.travelq.service;

import com.travelq.domain.model.TicketEntity;
import com.travelq.domain.repository.TicketRepository;
import com.travelq.domain.service.logic.TicketServiceImpl;
import com.travelq.dto.TicketDto;
import com.travelq.exception.TicketNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class TicketServiceTest {

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TicketServiceImpl ticketService;

    private TicketDto ticketDto;
    private TicketEntity ticketEntity;

    @BeforeEach
    void setUp() {
        ticketRepository = mock(TicketRepository.class);
        modelMapper = new ModelMapper();
        ticketService = new TicketServiceImpl(ticketRepository, modelMapper);

        ticketDto = new TicketDto(1L, LocalDateTime.now(), 1L, null, null);
        ticketEntity = modelMapper.map(ticketDto, TicketEntity.class);
    }

    @Test
    void testAddTicket() {
        when(ticketRepository.save(any(TicketEntity.class))).thenReturn(ticketEntity);

        TicketDto savedTicket = ticketService.addTicket(ticketDto);

        assertNotNull(savedTicket);
        assertEquals(ticketDto.getId(), savedTicket.getId());
        verify(ticketRepository, times(1)).save(any(TicketEntity.class));
    }

    @Test
    void testGetTicketById_WhenExists() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticketEntity));

        TicketDto result = ticketService.getTicketById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(ticketRepository, times(1)).findById(1L);
    }

    @Test
    void testGetTicketById_WhenNotExists() {
        when(ticketRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(TicketNotFoundException.class, () -> ticketService.getTicketById(2L));
        verify(ticketRepository, times(1)).findById(2L);
    }

    @Test
    void testGetAllTickets() {
        when(ticketRepository.findAll()).thenReturn(List.of(ticketEntity));

        List<TicketDto> tickets = ticketService.getAllTickets();

        assertEquals(1, tickets.size());
        assertEquals(ticketDto.getId(), tickets.get(0).getId());
        verify(ticketRepository, times(1)).findAll();
    }

    @Test
    void testUpdateTicket_WhenExists() {
        when(ticketRepository.findById(1L)).thenReturn(Optional.of(ticketEntity));
        when(ticketRepository.save(any(TicketEntity.class))).thenReturn(ticketEntity);

        TicketDto updatedTicket = ticketService.updateTicket(1L, ticketDto);

        assertNotNull(updatedTicket);
        assertEquals(1L, updatedTicket.getId());
        verify(ticketRepository).findById(1L);
        verify(ticketRepository).save(any(TicketEntity.class));
    }

    @Test
    void testUpdateTicket_WhenNotExists() {
        when(ticketRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(TicketNotFoundException.class, () -> ticketService.updateTicket(2L, ticketDto));
        verify(ticketRepository, times(1)).findById(2L);
    }

    @Test
    void testDeleteTicket_WhenExists() {
        when(ticketRepository.existsById(1L)).thenReturn(true);

        ticketService.deleteTicket(1L);

        verify(ticketRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteTicket_WhenNotExists() {
        when(ticketRepository.existsById(2L)).thenReturn(false);

        assertThrows(TicketNotFoundException.class, () -> ticketService.deleteTicket(2L));
        verify(ticketRepository, times(1)).existsById(2L);
    }
}