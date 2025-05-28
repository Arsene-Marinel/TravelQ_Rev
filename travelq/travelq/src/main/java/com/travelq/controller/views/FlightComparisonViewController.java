package com.travelq.controller.views;

import com.travelq.domain.service.FlightComparisonService;
import com.travelq.domain.service.FlightService;
import com.travelq.dto.FlightComparisonDto;
import com.travelq.dto.FlightDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/view/flight-comparisons")
@Slf4j
public class FlightComparisonViewController {

    private final FlightComparisonService flightComparisonService;
    private final FlightService flightService;

    @GetMapping
    public String listComparisons(Model model) {
        List<FlightComparisonDto> comparisons = flightComparisonService.getAllFlightComparisons();
        model.addAttribute("comparisonList", comparisons);
        return "flightComparisonList";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("flightComparison", new FlightComparisonDto());
        model.addAttribute("flights", flightService.getAllFlights());
        return "flightComparisonForm";
    }

    @PostMapping("/add")
    public String addComparison(
            @RequestParam("flights") List<Long> flightsById,
            Model model
    ) {
        // Caută zborurile după ID-uri
        List<FlightDto> selectedFlights = flightService.getFlightsByIds(flightsById);

        FlightComparisonDto dto = new FlightComparisonDto();
        dto.setFlights(selectedFlights);

        flightComparisonService.addFlightComparison(dto);
        return "redirect:/view/flight-comparisons";
    }


    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable(value = "id") Long id, Model model) {
        FlightComparisonDto comparison = flightComparisonService.getFlightComparisonById(id);
        List<FlightDto> flights = flightService.getAllFlights();
        model.addAttribute("flightComparison", comparison);
        model.addAttribute("flights", flights);
        return "flightComparisonEdit";
    }

    @PostMapping("/edit/{id}")
    public String updateComparison(@PathVariable Long id, @ModelAttribute FlightComparisonDto flightComparisonDto) {
        flightComparisonService.updateFlightComparison(id, flightComparisonDto);
        return "redirect:/view/flight-comparisons";
    }

    @GetMapping("/delete/{id}")
    public String deleteComparison(@PathVariable Long id) {
        flightComparisonService.deleteFlightComparison(id);
        return "redirect:/view/flight-comparisons";
    }
}
