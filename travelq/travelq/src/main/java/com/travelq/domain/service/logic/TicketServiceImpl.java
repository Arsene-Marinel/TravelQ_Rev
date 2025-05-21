package com.travelq.domain.service.logic;


import com.travelq.dto.TicketDto;
import com.travelq.exception.TicketNotFoundException;
import com.travelq.domain.model.TicketEntity;
import com.travelq.domain.repository.TicketRepository;
import com.travelq.domain.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final ModelMapper modelMapper;

    @Override
    public TicketDto addTicket(TicketDto ticketDto) {
        TicketEntity ticketEntity = modelMapper.map(ticketDto, TicketEntity.class);
        TicketEntity saved = ticketRepository.save(ticketEntity);
        return modelMapper.map(saved, TicketDto.class);
    }

    @Override
    public List<TicketDto> getAllTickets() {
        List<TicketEntity> ticketList = ticketRepository.findAll();
        return ticketList.stream()
                .map(ticketEntity -> modelMapper.map(ticketEntity, TicketDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public TicketDto getTicketById(Long ticketId) {
        TicketEntity ticketEntity = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(ticketId));
        return modelMapper.map(ticketEntity, TicketDto.class);
    }

    @Override
    public TicketDto updateTicket(Long ticketId, TicketDto ticketDto) {
        TicketEntity existingEntity = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new TicketNotFoundException(ticketId));
        modelMapper.map(ticketDto, existingEntity);
        TicketEntity updatedEntity = ticketRepository.save(existingEntity);
        return modelMapper.map(updatedEntity, TicketDto.class);
    }

    @Override
    public void deleteTicket(Long ticketId) {
        if (!ticketRepository.existsById(ticketId)) {
            throw new TicketNotFoundException(ticketId);
        }
        ticketRepository.deleteById(ticketId);
    }
}
