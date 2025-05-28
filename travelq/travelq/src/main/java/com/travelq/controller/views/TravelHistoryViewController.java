package com.travelq.controller.views;

import com.travelq.domain.service.TravelHistoryService;
import com.travelq.dto.TravelHistoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/view/travel-histories")
public class TravelHistoryViewController {

    private final TravelHistoryService travelHistoryService;

    @GetMapping
    public String listTravelHistories(Model model) {
        model.addAttribute("travelHistoryList", travelHistoryService.getAllTravelHistories());
        return "travelHistoryList";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("travelHistory", new TravelHistoryDto());
        return "travelHistoryForm";
    }

    @PostMapping("/add")
    public String addTravelHistory(@ModelAttribute("travelHistory") TravelHistoryDto dto) {
        travelHistoryService.addTravelHistory(dto);
        return "redirect:/view/travel-histories";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("travelHistory", travelHistoryService.getTravelHistoryById(id));
        return "travelHistoryEdit";
    }

    @PostMapping("/edit/{id}")
    public String updateTravelHistory(@PathVariable Long id, @ModelAttribute TravelHistoryDto dto) {
        travelHistoryService.updateTravelHistory(id, dto);
        return "redirect:/view/travel-histories";
    }

    @GetMapping("/delete/{id}")
    public String deleteTravelHistory(@PathVariable Long id) {
        travelHistoryService.deleteTravelHistory(id);
        return "redirect:/view/travel-histories";
    }
}
