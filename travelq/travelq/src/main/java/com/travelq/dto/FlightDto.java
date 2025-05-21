package com.travelq.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FlightDto {
    private Long id;
    private String origin;
    private String destination;
    private LocalDateTime departureTime;
    private LocalDateTime arrivalTime;
    private BigDecimal price;
    private int stopovers;
    private List<TicketDto> tickets;
    private List<FlightComparisonDto> comparisons;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FlightDto flightDto = (FlightDto) o;
        return stopovers == flightDto.stopovers && 
               Objects.equals(id, flightDto.id) && 
               Objects.equals(origin, flightDto.origin) && 
               Objects.equals(destination, flightDto.destination) && 
               Objects.equals(departureTime, flightDto.departureTime) && 
               Objects.equals(arrivalTime, flightDto.arrivalTime) && 
               Objects.equals(price, flightDto.price) && 
               Objects.equals(tickets, flightDto.tickets) && 
               Objects.equals(comparisons, flightDto.comparisons);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, origin, destination, departureTime, arrivalTime, price, stopovers, tickets, comparisons);
    }
}
