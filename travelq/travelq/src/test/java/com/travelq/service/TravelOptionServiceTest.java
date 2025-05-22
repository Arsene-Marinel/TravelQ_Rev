package com.travelq.service;

import com.travelq.domain.model.TravelOptionEntity;
import com.travelq.domain.repository.TravelOptionRepository;
import com.travelq.domain.service.logic.TravelOptionServiceImpl;
import com.travelq.dto.TravelOptionDto;
import com.travelq.exception.TravelOptionNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.ActiveProfiles;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class TravelOptionServiceTest {

    @Mock
    private TravelOptionRepository travelOptionRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TravelOptionServiceImpl travelOptionService;

    private TravelOptionDto createDto(Long id) {
        return new TravelOptionDto(
                id,
                true,
                "ECONOMY",
                "OVERSIZE_BAGGAGE",
                100L
        );
    }

    private TravelOptionEntity createEntity(Long id) {
        TravelOptionEntity entity = new TravelOptionEntity();
        entity.setId(id);
        entity.setCheckIn(true);
        entity.setSeatSelection(null); // You can adjust this if enums are available
        entity.setExtraBaggage(null);
        entity.setTicket(null); // Ticket not mocked here
        return entity;
    }

    @Test
    void testAddTravelOption() {
        TravelOptionDto dto = createDto(null);
        TravelOptionEntity entity = createEntity(null);
        TravelOptionEntity savedEntity = createEntity(1L);
        TravelOptionDto savedDto = createDto(1L);

        when(modelMapper.map(dto, TravelOptionEntity.class)).thenReturn(entity);
        when(travelOptionRepository.save(entity)).thenReturn(savedEntity);
        when(modelMapper.map(savedEntity, TravelOptionDto.class)).thenReturn(savedDto);

        TravelOptionDto result = travelOptionService.addTravelOption(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(travelOptionRepository).save(entity);
    }

    @Test
    void testGetAllTravelOptions() {
        List<TravelOptionEntity> entities = Arrays.asList(createEntity(1L), createEntity(2L));
        when(travelOptionRepository.findAll()).thenReturn(entities);
        when(modelMapper.map(any(TravelOptionEntity.class), eq(TravelOptionDto.class)))
                .thenAnswer(invocation -> {
                    TravelOptionEntity source = invocation.getArgument(0);
                    return createDto(source.getId());
                });

        List<TravelOptionDto> result = travelOptionService.getAllTravelOptions();

        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        verify(travelOptionRepository).findAll();
    }

    @Test
    void testGetTravelOptionById_Found() {
        TravelOptionEntity entity = createEntity(1L);
        TravelOptionDto dto = createDto(1L);

        when(travelOptionRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(modelMapper.map(entity, TravelOptionDto.class)).thenReturn(dto);

        TravelOptionDto result = travelOptionService.getTravelOptionById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetTravelOptionById_NotFound() {
        when(travelOptionRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(TravelOptionNotFoundException.class, () -> travelOptionService.getTravelOptionById(999L));
    }

    @Test
    void testUpdateTravelOption_Found() {
        Long id = 1L;
        TravelOptionDto dto = createDto(id);
        TravelOptionEntity entity = createEntity(id);

        when(travelOptionRepository.findById(id)).thenReturn(Optional.of(entity));
        doNothing().when(modelMapper).map(dto, entity);
        when(travelOptionRepository.save(entity)).thenReturn(entity);
        when(modelMapper.map(entity, TravelOptionDto.class)).thenReturn(dto);

        TravelOptionDto result = travelOptionService.updateTravelOption(id, dto);

        assertNotNull(result);
        assertEquals(id, result.getId());
        verify(travelOptionRepository).save(entity);
    }

    @Test
    void testUpdateTravelOption_NotFound() {
        Long id = 404L;
        TravelOptionDto dto = createDto(id);

        when(travelOptionRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(TravelOptionNotFoundException.class, () -> travelOptionService.updateTravelOption(id, dto));
    }

    @Test
    void testDeleteTravelOption_Exists() {
        Long id = 1L;

        when(travelOptionRepository.existsById(id)).thenReturn(true);
        doNothing().when(travelOptionRepository).deleteById(id);

        travelOptionService.deleteTravelOption(id);

        verify(travelOptionRepository).deleteById(id);
    }

    @Test
    void testDeleteTravelOption_NotExists() {
        Long id = 999L;

        when(travelOptionRepository.existsById(id)).thenReturn(false);

        assertThrows(TravelOptionNotFoundException.class, () -> travelOptionService.deleteTravelOption(id));
    }
}
