package com.travelq.controller;

import com.travelq.dto.TravelHistoryDto;
import com.travelq.dto.StandardResponse;
import com.travelq.domain.service.TravelHistoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/travelhistories")
@Slf4j
public class TravelHistoryController {

    private final TravelHistoryService travelHistoryService;

    @PostMapping
    public ResponseEntity<TravelHistoryDto> create(@Valid @RequestBody TravelHistoryDto travelHistoryDto) {
        log.info("Creating new travel history");
        TravelHistoryDto created = travelHistoryService.addTravelHistory(travelHistoryDto);
        log.info("Travel history created with ID: {}", created.getId());
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<TravelHistoryDto>> getAll() {
        log.info("Fetching all travel histories");
        List<TravelHistoryDto> histories = travelHistoryService.getAllTravelHistories();
        log.info("Retrieved {} travel histories", histories.size());
        return ResponseEntity.ok(histories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TravelHistoryDto> getById(@PathVariable Long id) {
        log.info("Fetching travel history with ID: {}", id);
        TravelHistoryDto history = travelHistoryService.getTravelHistoryById(id);
        log.info("Travel history found with ID: {}", id);
        return ResponseEntity.ok(history);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TravelHistoryDto> update(@PathVariable Long id, @Valid @RequestBody TravelHistoryDto travelHistoryDto) {
        log.info("Updating travel history with ID: {}", id);
        TravelHistoryDto updated = travelHistoryService.updateTravelHistory(id, travelHistoryDto);
        log.info("Travel history updated with ID: {}", id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StandardResponse> delete(@PathVariable Long id) {
        log.info("Deleting travel history with ID: {}", id);
        travelHistoryService.deleteTravelHistory(id);
        log.info("Travel history deleted with ID: {}", id);
        return ResponseEntity.ok(StandardResponse.success("TravelHistory with ID " + id + " was deleted."));
    }
}
