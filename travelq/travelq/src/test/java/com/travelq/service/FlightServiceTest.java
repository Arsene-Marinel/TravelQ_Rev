package com.travelq.service;

import com.travelq.domain.model.FlightEntity;
import com.travelq.domain.repository.FlightRepository;
import com.travelq.domain.service.logic.FlightServiceImpl;
import com.travelq.dto.FlightDto;
import com.travelq.exception.FlightNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.*;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class FlightServiceTest {

    @Mock
    private FlightRepository flightRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private FlightServiceImpl flightService;

    private FlightEntity createFlightEntity(Long id) {
        return new FlightEntity(id, "Paris", "Rome", LocalDateTime.now(), LocalDateTime.now().plusHours(2),
                new BigDecimal("199.99"), 0, null, null);
    }

    private FlightDto createFlightDto(Long id) {
        return new FlightDto(id, "Paris", "Rome", LocalDateTime.now(), LocalDateTime.now().plusHours(2),
                new BigDecimal("199.99"), 0, null, null);
    }

    @Test
    void testAddFlight() {
        FlightDto dto = createFlightDto(null);
        FlightEntity entity = createFlightEntity(null);
        FlightEntity saved = createFlightEntity(1L);
        FlightDto savedDto = createFlightDto(1L);

        when(modelMapper.map(dto, FlightEntity.class)).thenReturn(entity);
        when(flightRepository.save(entity)).thenReturn(saved);
        when(modelMapper.map(saved, FlightDto.class)).thenReturn(savedDto);

        FlightDto result = flightService.addFlight(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(flightRepository).save(entity);
    }

    @Test
    void testGetAllFlights() {
        List<FlightEntity> entities = Arrays.asList(createFlightEntity(1L), createFlightEntity(2L));
        when(flightRepository.findAll()).thenReturn(entities);
        when(modelMapper.map(any(FlightEntity.class), eq(FlightDto.class)))
                .thenAnswer(invocation -> {
                    FlightEntity source = invocation.getArgument(0);
                    return createFlightDto(source.getId());
                });

        List<FlightDto> result = flightService.getAllFlights();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
    }

    @Test
    void testGetAllFlightsPaginated() {
        Pageable pageable = PageRequest.of(0, 2);
        List<FlightEntity> entities = Arrays.asList(createFlightEntity(1L), createFlightEntity(2L));
        Page<FlightEntity> page = new PageImpl<>(entities, pageable, entities.size());

        when(flightRepository.findAll(pageable)).thenReturn(page);
        when(modelMapper.map(any(FlightEntity.class), eq(FlightDto.class)))
                .thenAnswer(invocation -> {
                    FlightEntity source = invocation.getArgument(0);
                    return createFlightDto(source.getId());
                });

        Page<FlightDto> result = flightService.getAllFlightsPaginated(pageable);

        assertEquals(2, result.getTotalElements());
    }

    @Test
    void testGetFlightById_Found() {
        FlightEntity entity = createFlightEntity(1L);
        FlightDto dto = createFlightDto(1L);

        when(flightRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(modelMapper.map(entity, FlightDto.class)).thenReturn(dto);

        FlightDto result = flightService.getFlightById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetFlightById_NotFound() {
        when(flightRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(FlightNotFoundException.class, () -> flightService.getFlightById(1L));
    }

    @Test
    void testUpdateFlight_Found() {
        Long id = 1L;
        FlightDto dto = createFlightDto(id);
        FlightEntity existing = createFlightEntity(id);
        FlightEntity updated = createFlightEntity(id);

        when(flightRepository.findById(id)).thenReturn(Optional.of(existing));
        doNothing().when(modelMapper).map(dto, existing);
        when(flightRepository.save(existing)).thenReturn(updated);
        when(modelMapper.map(updated, FlightDto.class)).thenReturn(dto);

        FlightDto result = flightService.updateFlight(id, dto);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void testUpdateFlight_NotFound() {
        when(flightRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(FlightNotFoundException.class, () -> flightService.updateFlight(2L, createFlightDto(2L)));
    }

    @Test
    void testDeleteFlight_Exists() {
        when(flightRepository.existsById(1L)).thenReturn(true);
        doNothing().when(flightRepository).deleteById(1L);

        flightService.deleteFlight(1L);

        verify(flightRepository).deleteById(1L);
    }

    @Test
    void testDeleteFlight_NotExists() {
        when(flightRepository.existsById(999L)).thenReturn(false);

        assertThrows(FlightNotFoundException.class, () -> flightService.deleteFlight(999L));
    }
}
