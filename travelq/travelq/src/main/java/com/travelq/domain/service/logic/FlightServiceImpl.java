package com.travelq.domain.service.logic;

import com.travelq.dto.FlightDto;
import com.travelq.exception.FlightNotFoundException;
import com.travelq.domain.model.FlightEntity;
import com.travelq.domain.repository.FlightRepository;
import com.travelq.domain.service.FlightService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FlightServiceImpl implements FlightService {

    private final FlightRepository flightRepository;
    private final ModelMapper modelMapper;

    @Override
    public FlightDto addFlight(FlightDto flightDto) {
        log.debug("Converting FlightDto to FlightEntity for saving");
        FlightEntity flightEntity = modelMapper.map(flightDto, FlightEntity.class);
        log.debug("Saving FlightEntity to database");
        FlightEntity saved = flightRepository.save(flightEntity);
        log.debug("Flight saved with ID: {}", saved.getId());
        return modelMapper.map(saved, FlightDto.class);
    }

    @Override
    public List<FlightDto> getAllFlights() {
        log.debug("Retrieving all flights from database");
        List<FlightEntity> flightList = flightRepository.findAll();
        log.debug("Found {} flights", flightList.size());
        return flightList.stream()
                .map(flightEntity -> modelMapper.map(flightEntity, FlightDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public Page<FlightDto> getAllFlightsPaginated(Pageable pageable) {
        log.debug("Retrieving paginated flights with pageable: {}", pageable);
        Page<FlightEntity> flightPage = flightRepository.findAll(pageable);
        log.debug("Found {} flights in page {} of {}", 
                flightPage.getNumberOfElements(), 
                flightPage.getNumber() + 1, 
                flightPage.getTotalPages());
        return flightPage.map(flightEntity -> modelMapper.map(flightEntity, FlightDto.class));
    }

    @Override
    public FlightDto getFlightById(Long flightId) {
        log.debug("Looking for flight with ID: {}", flightId);
        FlightEntity flightEntity = flightRepository.findById(flightId)
                .orElseThrow(() -> {
                    log.error("Flight not found with ID: {}", flightId);
                    return new FlightNotFoundException(flightId);
                });
        log.debug("Flight found with ID: {}", flightId);
        return modelMapper.map(flightEntity, FlightDto.class);
    }

    @Override
    public FlightDto updateFlight(Long flightId, FlightDto flightDto) {
        log.debug("Looking for flight to update with ID: {}", flightId);
        FlightEntity existingFlight = flightRepository.findById(flightId)
                .orElseThrow(() -> {
                    log.error("Flight not found for update with ID: {}", flightId);
                    return new FlightNotFoundException(flightId);
                });
        log.debug("Updating flight properties");
        modelMapper.map(flightDto, existingFlight);
        log.debug("Saving updated flight to database");
        FlightEntity updatedFlight = flightRepository.save(existingFlight);
        log.debug("Flight updated with ID: {}", updatedFlight.getId());
        return modelMapper.map(updatedFlight, FlightDto.class);
    }

    @Override
    public void deleteFlight(Long flightId) {
        log.debug("Checking if flight exists with ID: {}", flightId);
        if (!flightRepository.existsById(flightId)) {
            log.error("Flight not found for deletion with ID: {}", flightId);
            throw new FlightNotFoundException(flightId);
        }
        log.debug("Deleting flight with ID: {}", flightId);
        flightRepository.deleteById(flightId);
        log.debug("Flight deleted with ID: {}", flightId);
    }

    @Override
    public List<FlightDto> getFlightsByIds(List<Long> ids) {
        List<FlightEntity> flights = flightRepository.findAllById(ids);
        return flights.stream()
                .map(flight -> modelMapper.map(flight, FlightDto.class))
                .toList();
    }

}
