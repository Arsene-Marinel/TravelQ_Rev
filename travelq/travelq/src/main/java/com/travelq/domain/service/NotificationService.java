package com.travelq.domain.service;

import com.travelq.dto.NotificationDto;

import java.util.List;

public interface NotificationService {

    NotificationDto addNotification(NotificationDto notificationDto);

    List<NotificationDto> getAllNotifications();

    NotificationDto getNotificationById(Long notificationId);
    NotificationDto updateNotification(Long notificationId, NotificationDto notificationDto);

    void deleteNotification(Long notificationId);
}
