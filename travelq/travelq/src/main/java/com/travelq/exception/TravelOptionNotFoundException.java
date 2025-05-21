package com.travelq.exception;

public class TravelOptionNotFoundException extends RuntimeException{

    public TravelOptionNotFoundException(Long id) {
        super("TravelOption not found with id: " + id);
    }
}
