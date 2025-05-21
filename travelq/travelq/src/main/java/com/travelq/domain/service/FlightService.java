package com.travelq.domain.service;

import com.travelq.dto.FlightDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FlightService {

    FlightDto addFlight(FlightDto flightDto);

    List<FlightDto> getAllFlights();
    
    Page<FlightDto> getAllFlightsPaginated(Pageable pageable);

    FlightDto getFlightById(Long flightId);

    FlightDto updateFlight(Long flightId, FlightDto flightDto);

    void deleteFlight(Long flightId);
}
