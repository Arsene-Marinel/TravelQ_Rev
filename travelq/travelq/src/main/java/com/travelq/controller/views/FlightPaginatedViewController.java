package com.travelq.controller.views;

import com.travelq.domain.service.FlightService;
import com.travelq.dto.FlightDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/view/flights-paginated")
@RequiredArgsConstructor
@Slf4j
public class FlightPaginatedViewController {

    private final FlightService flightService;

    @GetMapping
    public String listFlights(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String direction,
            Model model) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(direction), sortBy));
        Page<FlightDto> flights = flightService.getAllFlightsPaginated(pageable);

        model.addAttribute("flights", flights);
        return "flightPaginated";
    }
}

