package com.travelq.service;

import com.travelq.domain.model.FlightComparisonEntity;
import com.travelq.domain.repository.FlightComparisonRepository;
import com.travelq.domain.service.logic.FlightComparisonServiceImpl;
import com.travelq.dto.FlightComparisonDto;
import com.travelq.exception.FlightComparisonNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class FlightComparisonServiceTest {

    @Mock
    private FlightComparisonRepository flightcomparisonRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private FlightComparisonServiceImpl flightComparisonService;

    @Test
    void testAddFlightComparison() {
        FlightComparisonDto dto = new FlightComparisonDto();
        FlightComparisonEntity entity = new FlightComparisonEntity();
        FlightComparisonEntity savedEntity = new FlightComparisonEntity();
        savedEntity.setId(1L);
        FlightComparisonDto savedDto = new FlightComparisonDto();
        savedDto.setId(1L);

        when(modelMapper.map(dto, FlightComparisonEntity.class)).thenReturn(entity);
        when(flightcomparisonRepository.save(any(FlightComparisonEntity.class))).thenReturn(savedEntity);
        when(modelMapper.map(savedEntity, FlightComparisonDto.class)).thenReturn(savedDto);

        FlightComparisonDto result = flightComparisonService.addFlightComparison(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(flightcomparisonRepository, times(1)).save(any(FlightComparisonEntity.class));
    }

    @Test
    void testGetAllFlightComparisons() {
        FlightComparisonEntity entity1 = new FlightComparisonEntity();
        entity1.setId(1L);
        FlightComparisonEntity entity2 = new FlightComparisonEntity();
        entity2.setId(2L);

        when(flightcomparisonRepository.findAll()).thenReturn(Arrays.asList(entity1, entity2));
        when(modelMapper.map(entity1, FlightComparisonDto.class)).thenReturn(new FlightComparisonDto(1L, null));
        when(modelMapper.map(entity2, FlightComparisonDto.class)).thenReturn(new FlightComparisonDto(2L, null));

        List<FlightComparisonDto> result = flightComparisonService.getAllFlightComparisons();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
    }

    @Test
    void testGetFlightComparisonById_Found() {
        Long id = 1L;
        FlightComparisonEntity entity = new FlightComparisonEntity();
        entity.setId(id);
        FlightComparisonDto dto = new FlightComparisonDto(id, null);

        when(flightcomparisonRepository.findById(id)).thenReturn(Optional.of(entity));
        when(modelMapper.map(entity, FlightComparisonDto.class)).thenReturn(dto);

        FlightComparisonDto result = flightComparisonService.getFlightComparisonById(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
    }

    @Test
    void testGetFlightComparisonById_NotFound() {
        Long id = 999L;
        when(flightcomparisonRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(FlightComparisonNotFoundException.class, () -> {
            flightComparisonService.getFlightComparisonById(id);
        });
    }

    @Test
    void testUpdateFlightComparison_Found() {
        Long id = 1L;
        FlightComparisonDto dto = new FlightComparisonDto(id, null);
        FlightComparisonEntity existing = new FlightComparisonEntity();
        existing.setId(id);
        FlightComparisonEntity updated = new FlightComparisonEntity();
        updated.setId(id);

        when(flightcomparisonRepository.findById(id)).thenReturn(Optional.of(existing));
        doNothing().when(modelMapper).map(dto, existing);  // mapare în același obiect
        when(flightcomparisonRepository.save(existing)).thenReturn(updated);
        when(modelMapper.map(updated, FlightComparisonDto.class)).thenReturn(dto);

        FlightComparisonDto result = flightComparisonService.updateFlightComparison(id, dto);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(flightcomparisonRepository).save(existing);
    }

    @Test
    void testUpdateFlightComparison_NotFound() {
        Long id = 404L;
        FlightComparisonDto dto = new FlightComparisonDto();

        when(flightcomparisonRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(FlightComparisonNotFoundException.class, () ->
                flightComparisonService.updateFlightComparison(id, dto)
        );
    }

    @Test
    void testDeleteFlightComparison_Exists() {
        Long id = 1L;

        when(flightcomparisonRepository.existsById(id)).thenReturn(true);
        doNothing().when(flightcomparisonRepository).deleteById(id);

        flightComparisonService.deleteFlightComparison(id);

        verify(flightcomparisonRepository).deleteById(id);
    }

    @Test
    void testDeleteFlightComparison_NotExists() {
        Long id = 2L;
        when(flightcomparisonRepository.existsById(id)).thenReturn(false);

        assertThrows(FlightComparisonNotFoundException.class, () ->
                flightComparisonService.deleteFlightComparison(id)
        );
    }
}
