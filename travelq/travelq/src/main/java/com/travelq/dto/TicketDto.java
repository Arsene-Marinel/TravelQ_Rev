package com.travelq.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TicketDto {
    private Long id;
    private LocalDateTime purchaseDate;
    private UserDto user;
    private FlightDto flight;
    private TravelOptionDto travelOption;
}
