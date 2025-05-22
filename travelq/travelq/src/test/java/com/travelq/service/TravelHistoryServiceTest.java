package com.travelq.service;

import com.travelq.domain.model.TravelHistoryEntity;
import com.travelq.domain.model.UserEntity;
import com.travelq.domain.repository.TravelHistoryRepository;
import com.travelq.domain.service.logic.TravelHistoryServiceImpl;
import com.travelq.dto.TravelHistoryDto;
import com.travelq.dto.UserDto;
import com.travelq.exception.TravelHistoryNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class TravelHistoryServiceTest {

    @Mock
    private TravelHistoryRepository repository;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private TravelHistoryServiceImpl service;

    @BeforeEach
    void setUp() {
        repository = mock(TravelHistoryRepository.class);
        modelMapper = new ModelMapper();
        service = new TravelHistoryServiceImpl(repository, modelMapper);
    }

    @Test
    void testAddTravelHistory() {
        TravelHistoryDto dto = new TravelHistoryDto(null, 5, 1234.50, new UserDto());
        TravelHistoryEntity entity = modelMapper.map(dto, TravelHistoryEntity.class);
        entity.setId(1L);

        when(repository.save(any())).thenReturn(entity);

        TravelHistoryDto result = service.addTravelHistory(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(5, result.getFlightsCount());
        assertEquals(1234.50, result.getTotalSpent());
        verify(repository, times(1)).save(any(TravelHistoryEntity.class));
    }

    @Test
    void testGetAllTravelHistories() {
        TravelHistoryEntity entity1 = new TravelHistoryEntity(1L, 2, new BigDecimal("450.00"), new UserEntity());
        TravelHistoryEntity entity2 = new TravelHistoryEntity(2L, 3, new BigDecimal("880.00"), new UserEntity());

        when(repository.findAll()).thenReturn(List.of(entity1, entity2));

        List<TravelHistoryDto> results = service.getAllTravelHistories();

        assertEquals(2, results.size());
        assertEquals(2, results.get(0).getFlightsCount());
        assertEquals(3, results.get(1).getFlightsCount());
    }

    @Test
    void testGetTravelHistoryById_WhenExists() {
        TravelHistoryEntity entity = new TravelHistoryEntity(1L, 4, new BigDecimal("1000.00"), new UserEntity());

        when(repository.findById(1L)).thenReturn(Optional.of(entity));

        TravelHistoryDto result = service.getTravelHistoryById(1L);

        assertEquals(4, result.getFlightsCount());
        assertEquals(1000.00, result.getTotalSpent());
    }

    @Test
    void testGetTravelHistoryById_WhenNotFound() {
        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TravelHistoryNotFoundException.class, () -> service.getTravelHistoryById(1L));
    }

    @Test
    void testUpdateTravelHistory_WhenExists() {
        TravelHistoryEntity existing = new TravelHistoryEntity(1L, 2, new BigDecimal("600.00"), new UserEntity());
        TravelHistoryDto updateDto = new TravelHistoryDto(1L, 6, 1900.00, new UserDto());

        when(repository.findById(1L)).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        TravelHistoryDto result = service.updateTravelHistory(1L, updateDto);

        assertEquals(6, result.getFlightsCount());
        assertEquals(1900.00, result.getTotalSpent());
    }

    @Test
    void testUpdateTravelHistory_WhenNotFound() {
        TravelHistoryDto dto = new TravelHistoryDto(1L, 6, 2000.00, new UserDto());

        when(repository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(TravelHistoryNotFoundException.class, () -> service.updateTravelHistory(1L, dto));
    }

    @Test
    void testDeleteTravelHistory_WhenExists() {
        when(repository.existsById(1L)).thenReturn(true);

        service.deleteTravelHistory(1L);

        verify(repository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteTravelHistory_WhenNotFound() {
        when(repository.existsById(1L)).thenReturn(false);

        assertThrows(TravelHistoryNotFoundException.class, () -> service.deleteTravelHistory(1L));
    }
}

