package com.travelq.domain.service.logic;

import com.travelq.dto.FlightComparisonDto;
import com.travelq.exception.FlightComparisonNotFoundException;
import com.travelq.domain.model.FlightComparisonEntity;
import com.travelq.domain.repository.FlightComparisonRepository;
import com.travelq.domain.service.FlightComparisonService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FlightComparisonServiceImpl implements FlightComparisonService {

    private final FlightComparisonRepository flightcomparisonRepository;
    private final ModelMapper modelMapper;

    @Override
    public FlightComparisonDto addFlightComparison(FlightComparisonDto flightcomparisonDto) {
        FlightComparisonEntity flightComparisonEntity = modelMapper.map(flightcomparisonDto, FlightComparisonEntity.class);
        FlightComparisonEntity saved = flightcomparisonRepository.save(flightComparisonEntity);
        return modelMapper.map(saved, FlightComparisonDto.class);
    }

    @Override
    public List<FlightComparisonDto> getAllFlightComparisons() {
        List<FlightComparisonEntity> flightComparisonList = flightcomparisonRepository.findAll();
        return flightComparisonList.stream()
                .map(e -> modelMapper.map(e, FlightComparisonDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public FlightComparisonDto getFlightComparisonById(Long flightComparisonId) {
        FlightComparisonEntity flightComparisonEntity = flightcomparisonRepository.findById(flightComparisonId)
                .orElseThrow(() -> new FlightComparisonNotFoundException(flightComparisonId));
        return modelMapper.map(flightComparisonEntity, FlightComparisonDto.class);
    }

    @Override
    public FlightComparisonDto updateFlightComparison(Long flightComparisonId, FlightComparisonDto flightcomparisonDto) {
        FlightComparisonEntity existingEntity = flightcomparisonRepository.findById(flightComparisonId)
                .orElseThrow(() -> new FlightComparisonNotFoundException(flightComparisonId));
        modelMapper.map(flightcomparisonDto, existingEntity);
        FlightComparisonEntity updatedEntity = flightcomparisonRepository.save(existingEntity);
        return modelMapper.map(updatedEntity, FlightComparisonDto.class);
    }

    @Override
    public void deleteFlightComparison(Long flightComparisonId) {
        if (!flightcomparisonRepository.existsById(flightComparisonId)) {
            throw new FlightComparisonNotFoundException(flightComparisonId);
        }
        flightcomparisonRepository.deleteById(flightComparisonId);
    }
}
