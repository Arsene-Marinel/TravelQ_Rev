package com.travelq.controller.views;

import com.travelq.domain.service.NotificationService;
import com.travelq.dto.NotificationDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/view/notifications")
@Slf4j
public class NotificationViewController {

    private final NotificationService notificationService;

    @GetMapping
    public String listNotifications(Model model) {
        model.addAttribute("notificationList", notificationService.getAllNotifications());
        return "notificationList";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("notification", new NotificationDto());
        return "notificationForm";
    }

    @PostMapping("/add")
    public String addNotification(@ModelAttribute NotificationDto notificationDto) {
        notificationService.addNotification(notificationDto);
        return "redirect:/view/notifications";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("notification", notificationService.getNotificationById(id));
        return "notificationEdit";
    }

    @PostMapping("/edit/{id}")
    public String updateNotification(@PathVariable Long id, @ModelAttribute NotificationDto notificationDto) {
        notificationService.updateNotification(id, notificationDto);
        return "redirect:/view/notifications";
    }

    @GetMapping("/delete/{id}")
    public String deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return "redirect:/view/notifications";
    }
}
