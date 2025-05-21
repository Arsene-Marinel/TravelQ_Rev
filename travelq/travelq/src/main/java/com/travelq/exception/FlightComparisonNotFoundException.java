package com.travelq.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class FlightComparisonNotFoundException extends RuntimeException {
    public FlightComparisonNotFoundException(Long id) {
        super("FlightComparison not found with id: " + id);
    }
}
