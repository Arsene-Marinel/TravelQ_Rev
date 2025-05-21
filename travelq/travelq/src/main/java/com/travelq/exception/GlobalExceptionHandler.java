package com.travelq.exception;

import com.travelq.dto.StandardResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(FlightNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<StandardResponse> handleFlightNotFoundException(FlightNotFoundException ex) {
        log.error("Flight not found", ex);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(StandardResponse.error(ex.getMessage()));
    }
    
    @ExceptionHandler(FlightComparisonNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<StandardResponse> handleFlightComparisonNotFoundException(FlightComparisonNotFoundException ex) {
        log.error("Flight comparison not found", ex);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(StandardResponse.error(ex.getMessage()));
    }
    
    @ExceptionHandler(NotificationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<StandardResponse> handleNotificationNotFoundException(NotificationNotFoundException ex) {
        log.error("Notification not found", ex);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(StandardResponse.error(ex.getMessage()));
    }
    
    @ExceptionHandler(TicketNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<StandardResponse> handleTicketNotFoundException(TicketNotFoundException ex) {
        log.error("Ticket not found", ex);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(StandardResponse.error(ex.getMessage()));
    }
    
    @ExceptionHandler(TravelHistoryNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<StandardResponse> handleTravelHistoryNotFoundException(TravelHistoryNotFoundException ex) {
        log.error("Travel history not found", ex);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(StandardResponse.error(ex.getMessage()));
    }
    
    @ExceptionHandler(TravelOptionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<StandardResponse> handleTravelOptionNotFoundException(TravelOptionNotFoundException ex) {
        log.error("Travel option not found", ex);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(StandardResponse.error(ex.getMessage()));
    }
    
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<StandardResponse> handleUserNotFoundException(UserNotFoundException ex) {
        log.error("User not found", ex);
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(StandardResponse.error(ex.getMessage()));
    }
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<StandardResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        StringBuilder errorMessage = new StringBuilder("Validation error: ");
        errors.forEach((field, message) -> 
            errorMessage.append(field).append(" - ").append(message).append("; "));
            
        log.error("Validation error: {}", errors);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(StandardResponse.error(errorMessage.toString()));
    }
    
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<StandardResponse> handleGeneralException(Exception ex) {
        log.error("Unexpected error", ex);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(StandardResponse.error("An unexpected error occurred: " + ex.getMessage()));
    }
} 