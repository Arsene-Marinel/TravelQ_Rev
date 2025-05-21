package com.travelq.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Standard response model for operations that don't return a data object
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class StandardResponse {
    private boolean success;
    private String message;
    
    public static StandardResponse success(String message) {
        return new StandardResponse(true, message);
    }
    
    public static StandardResponse error(String message) {
        return new StandardResponse(false, message);
    }
} 