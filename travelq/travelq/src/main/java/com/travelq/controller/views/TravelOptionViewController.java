package com.travelq.controller.views;

import com.travelq.domain.service.TravelOptionService;
import com.travelq.dto.TravelOptionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/view/travel-options")
@Slf4j
public class TravelOptionViewController {

    private final TravelOptionService travelOptionService;

    @GetMapping
    public String listTravelOptions(Model model) {
        model.addAttribute("travelOptionList", travelOptionService.getAllTravelOptions());
        return "travelOptionList";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("travelOption", new TravelOptionDto());
        return "travelOptionForm";
    }

    @PostMapping("/add")
    public String addTravelOption(@ModelAttribute("travelOption") TravelOptionDto dto) {
        travelOptionService.addTravelOption(dto);
        return "redirect:/view/travel-options";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("travelOption", travelOptionService.getTravelOptionById(id));
        return "travelOptionEdit";
    }

    @PostMapping("/edit/{id}")
    public String updateTravelOption(@PathVariable Long id, @ModelAttribute TravelOptionDto dto) {
        travelOptionService.updateTravelOption(id, dto);
        return "redirect:/view/travel-options";
    }

    @GetMapping("/delete/{id}")
    public String deleteTravelOption(@PathVariable Long id) {
        travelOptionService.deleteTravelOption(id);
        return "redirect:/view/travel-options";
    }
}

