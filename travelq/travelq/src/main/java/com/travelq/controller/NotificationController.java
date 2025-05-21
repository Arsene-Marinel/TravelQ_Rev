package com.travelq.controller;

import com.travelq.dto.NotificationDto;
import com.travelq.dto.StandardResponse;
import com.travelq.domain.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/notifications")
@Slf4j
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    public ResponseEntity<NotificationDto> create(@Valid @RequestBody NotificationDto notificationDto) {
        log.info("Creating new notification");
        NotificationDto created = notificationService.addNotification(notificationDto);
        log.info("Notification created with ID: {}", created.getId());
        return ResponseEntity.ok(created);
    }

    @GetMapping
    public ResponseEntity<List<NotificationDto>> getAll() {
        log.info("Fetching all notifications");
        List<NotificationDto> notifications = notificationService.getAllNotifications();
        log.info("Retrieved {} notifications", notifications.size());
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationDto> getById(@PathVariable Long id) {
        log.info("Fetching notification with ID: {}", id);
        NotificationDto notification = notificationService.getNotificationById(id);
        log.info("Notification found with ID: {}", id);
        return ResponseEntity.ok(notification);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotificationDto> update(@PathVariable Long id, @Valid @RequestBody NotificationDto notificationDto) {
        log.info("Updating notification with ID: {}", id);
        NotificationDto updated = notificationService.updateNotification(id, notificationDto);
        log.info("Notification updated with ID: {}", id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<StandardResponse> delete(@PathVariable Long id) {
        log.info("Deleting notification with ID: {}", id);
        notificationService.deleteNotification(id);
        log.info("Notification deleted with ID: {}", id);
        return ResponseEntity.ok(StandardResponse.success("Notification with ID " + id + " was deleted."));
    }
}
