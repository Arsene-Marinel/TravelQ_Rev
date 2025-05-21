package com.travelq.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TravelHistoryDto {
    private Long id;
    private int flightsCount;
    private double totalSpent;
    private UserDto user;
}
