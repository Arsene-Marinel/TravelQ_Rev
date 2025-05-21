package com.travelq.domain.service;

import com.travelq.dto.FlightComparisonDto;

import java.util.List;

public interface FlightComparisonService {

    FlightComparisonDto addFlightComparison(FlightComparisonDto flightcomparisonDto);

    List<FlightComparisonDto> getAllFlightComparisons();

    FlightComparisonDto getFlightComparisonById(Long flightComparisonId);

    FlightComparisonDto updateFlightComparison(Long flightComparisonId, FlightComparisonDto flightcomparisonDto);

    void deleteFlightComparison(Long flightComparisonId);
}
