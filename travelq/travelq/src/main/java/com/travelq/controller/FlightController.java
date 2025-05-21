package com.travelq.controller;

import com.travelq.dto.FlightDto;
import com.travelq.dto.StandardResponse;
import com.travelq.domain.service.FlightService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/flights")
@Slf4j
@Tag(name = "Flight Management", description = "APIs for managing flights")
public class FlightController {

    private final FlightService flightService;

    @Operation(summary = "Create a flight", description = "Adds a new flight to the system")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Flight created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = FlightDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content)
    })
    @PostMapping
    public ResponseEntity<FlightDto> createFlight(@Valid @RequestBody FlightDto flightDto) {
        log.info("Creating a new flight from {} to {}", flightDto.getOrigin(), flightDto.getDestination());
        FlightDto created = flightService.addFlight(flightDto);
        log.info("Flight created successfully with ID: {}", created.getId());
        return ResponseEntity.ok(created);
    }

    @Operation(summary = "Get all flights", description = "Retrieves a list of all flights")
    @ApiResponse(responseCode = "200", description = "List of flights retrieved successfully")
    @GetMapping
    public ResponseEntity<List<FlightDto>> getAllFlights() {
        log.info("Fetching all flights");
        List<FlightDto> flights = flightService.getAllFlights();
        log.info("Retrieved {} flights", flights.size());
        return ResponseEntity.ok(flights);
    }
    
    @Operation(summary = "Get paginated flights", description = "Retrieves a paginated list of flights with sorting options")
    @ApiResponse(responseCode = "200", description = "Paginated list of flights retrieved successfully")
    @GetMapping("/paged")
    public ResponseEntity<Page<FlightDto>> getAllFlightsPaginated(
            @Parameter(description = "Page number (0-based)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Page size") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sort field (e.g., id, price, origin, destination)") @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction (asc or desc)") @RequestParam(defaultValue = "asc") String direction) {
        
        log.info("Fetching paginated flights (page={}, size={}, sortBy={}, direction={})", page, size, sortBy, direction);
        Sort.Direction sortDirection = direction.equalsIgnoreCase("desc") ? 
                Sort.Direction.DESC : Sort.Direction.ASC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        Page<FlightDto> flightPage = flightService.getAllFlightsPaginated(pageable);
        log.info("Retrieved {} flights (page {} of {})", 
                flightPage.getNumberOfElements(), flightPage.getNumber() + 1, flightPage.getTotalPages());
        
        return ResponseEntity.ok(flightPage);
    }

    @Operation(summary = "Get flight by ID", description = "Retrieves a flight by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Flight found",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = FlightDto.class))),
        @ApiResponse(responseCode = "404", description = "Flight not found", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<FlightDto> getFlightById(
            @Parameter(description = "Flight ID", required = true) @PathVariable Long id) {
        log.info("Fetching flight with ID: {}", id);
        FlightDto flight = flightService.getFlightById(id);
        log.info("Flight found with ID: {}", id);
        return ResponseEntity.ok(flight);
    }

    @Operation(summary = "Update flight", description = "Updates an existing flight by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Flight updated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = FlightDto.class))),
        @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
        @ApiResponse(responseCode = "404", description = "Flight not found", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<FlightDto> updateFlight(
            @Parameter(description = "Flight ID", required = true) @PathVariable Long id, 
            @Valid @RequestBody FlightDto flightDto) {
        log.info("Updating flight with ID: {}", id);
        FlightDto updated = flightService.updateFlight(id, flightDto);
        log.info("Flight updated successfully with ID: {}", id);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Delete flight", description = "Deletes a flight by its ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Flight deleted successfully", 
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = StandardResponse.class))),
        @ApiResponse(responseCode = "404", description = "Flight not found", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<StandardResponse> deleteFlight(
            @Parameter(description = "Flight ID", required = true) @PathVariable Long id) {
        log.info("Deleting flight with ID: {}", id);
        flightService.deleteFlight(id);
        log.info("Flight deleted successfully with ID: {}", id);
        return ResponseEntity.ok(StandardResponse.success("Flight with ID " + id + " was deleted."));
    }
}
