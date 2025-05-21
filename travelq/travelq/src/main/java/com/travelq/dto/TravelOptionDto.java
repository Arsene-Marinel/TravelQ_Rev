package com.travelq.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TravelOptionDto {
    private Long id;
    private boolean checkIn;
    private String seatSelection;
    private String extraBaggage;
    private TicketDto ticket;
}
