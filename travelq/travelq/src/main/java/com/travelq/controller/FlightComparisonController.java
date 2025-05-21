package com.travelq.controller;

import com.travelq.dto.FlightComparisonDto;
import com.travelq.dto.StandardResponse;
import com.travelq.domain.service.FlightComparisonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/flightcomparisons")
@Slf4j
public class FlightComparisonController {

    private final FlightComparisonService flightComparisonService;

    @PostMapping
    public ResponseEntity<FlightComparisonDto> create(@Valid @RequestBody FlightComparisonDto flightComparisonDto) {
        log.info("Creating new flight comparison");
        FlightComparisonDto created = flightComparisonService.addFlightComparison(flightComparisonDto);
        log.info("Flight comparison created with ID: {}", created.getId());
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<FlightComparisonDto>> getAll() {
        log.info("Fetching all flight comparisons");
        List<FlightComparisonDto> comparisons = flightComparisonService.getAllFlightComparisons();
        log.info("Retrieved {} flight comparisons", comparisons.size());
        return ResponseEntity.ok(comparisons);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FlightComparisonDto> getById(@PathVariable Long id) {
        log.info("Fetching flight comparison with ID: {}", id);
        FlightComparisonDto comparison = flightComparisonService.getFlightComparisonById(id);
        log.info("Flight comparison found with ID: {}", id);
        return ResponseEntity.ok(comparison);
    }

    @PutMapping("/{id}")
    public ResponseEntity<FlightComparisonDto> update(@PathVariable Long id, @Valid @RequestBody FlightComparisonDto flightComparisonDto) {
        log.info("Updating flight comparison with ID: {}", id);
        FlightComparisonDto updated = flightComparisonService.updateFlightComparison(id, flightComparisonDto);
        log.info("Flight comparison updated with ID: {}", id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StandardResponse> delete(@PathVariable Long id) {
        log.info("Deleting flight comparison with ID: {}", id);
        flightComparisonService.deleteFlightComparison(id);
        log.info("Flight comparison deleted with ID: {}", id);
        return ResponseEntity.ok(StandardResponse.success("FlightComparison with ID " + id + " was deleted."));
    }
}
