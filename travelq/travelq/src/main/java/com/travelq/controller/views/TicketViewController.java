package com.travelq.controller.views;

import com.travelq.domain.service.TicketService;
import com.travelq.dto.TicketDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/view/tickets")
@Slf4j
public class TicketViewController {

    private final TicketService ticketService;

    @GetMapping
    public String listTickets(Model model) {
        model.addAttribute("ticketList", ticketService.getAllTickets());
        return "ticketList";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("ticket", new TicketDto());
        return "ticketForm";
    }

    @PostMapping("/add")
    public String addTicket(@ModelAttribute TicketDto ticketDto) {
        ticketService.addTicket(ticketDto);
        return "redirect:/view/tickets";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("ticket", ticketService.getTicketById(id));
        return "ticketEdit";
    }

    @PostMapping("/edit/{id}")
    public String updateTicket(@PathVariable Long id, @ModelAttribute TicketDto ticketDto) {
        ticketService.updateTicket(id, ticketDto);
        return "redirect:/view/tickets";
    }

    @GetMapping("/delete/{id}")
    public String deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return "redirect:/view/tickets";
    }
}

