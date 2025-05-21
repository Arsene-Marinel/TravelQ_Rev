package com.travelq.exception;

public class TravelHistoryNotFoundException extends RuntimeException{

    public TravelHistoryNotFoundException(Long id) {
        super("TravelHistory not found with id: " + id);
    }
}
