package com.travelq.controller;

import com.travelq.dto.TravelOptionDto;
import com.travelq.dto.StandardResponse;
import com.travelq.domain.service.TravelOptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/traveloptions")
@Slf4j
public class TravelOptionController {

    private final TravelOptionService travelOptionService;

    @PostMapping
    public ResponseEntity<TravelOptionDto> createTravelOption(@Valid @RequestBody TravelOptionDto travelOptionDto) {
        log.info("Creating new travel option");
        TravelOptionDto created = travelOptionService.addTravelOption(travelOptionDto);
        log.info("Travel option created with ID: {}", created.getId());
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<TravelOptionDto>> getAll() {
        log.info("Fetching all travel options");
        List<TravelOptionDto> options = travelOptionService.getAllTravelOptions();
        log.info("Retrieved {} travel options", options.size());
        return ResponseEntity.ok(options);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TravelOptionDto> getById(@PathVariable Long id) {
        log.info("Fetching travel option with ID: {}", id);
        TravelOptionDto option = travelOptionService.getTravelOptionById(id);
        log.info("Travel option found with ID: {}", id);
        return ResponseEntity.ok(option);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TravelOptionDto> update(@PathVariable Long id, @Valid @RequestBody TravelOptionDto travelOptionDto) {
        log.info("Updating travel option with ID: {}", id);
        TravelOptionDto updated = travelOptionService.updateTravelOption(id, travelOptionDto);
        log.info("Travel option updated with ID: {}", id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StandardResponse> delete(@PathVariable Long id) {
        log.info("Deleting travel option with ID: {}", id);
        travelOptionService.deleteTravelOption(id);
        log.info("Travel option deleted with ID: {}", id);
        return ResponseEntity.ok(StandardResponse.success("TravelOption with ID " + id + " was deleted."));
    }
}
