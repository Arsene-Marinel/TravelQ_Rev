package com.travelq.controller.views;

import com.travelq.domain.service.FlightService;
import com.travelq.dto.FlightDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/view/flights")
@Slf4j
public class FlightViewController {

    private final FlightService flightService;

    @GetMapping
    public String listFlights(Model model) {
        model.addAttribute("flightList", flightService.getAllFlights());
        return "flightList";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("flight", new FlightDto());
        return "flightForm";
    }

    @PostMapping("/add")
    public String addFlight(@ModelAttribute("flight") FlightDto flightDto) {
        flightService.addFlight(flightDto);
        return "redirect:/view/flights";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("flight", flightService.getFlightById(id));
        return "flightEdit";
    }

    @PostMapping("/edit/{id}")
    public String updateFlight(@PathVariable Long id, @ModelAttribute FlightDto flightDto) {
        flightService.updateFlight(id, flightDto);
        return "redirect:/view/flights";
    }

    @GetMapping("/delete/{id}")
    public String deleteFlight(@PathVariable Long id) {
        flightService.deleteFlight(id);
        return "redirect:/view/flights";
    }
}
