package com.travelq.service;

import com.travelq.domain.service.NotificationService;
import com.travelq.domain.service.UserService;
import com.travelq.dto.NotificationDto;
import com.travelq.dto.UserDto;
import com.travelq.exception.NotificationNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class NotificationServiceTestProfileTest {

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private UserService userService;

    private UserDto user;

    @BeforeEach
    public void setUp() {
        user = new UserDto();
        user.setUsername("notif_user");
        user.setEmail("notif@example.com");
        user.setPassword("1234");
        user.setFirstName("Notify");
        user.setLastName("User");

        user = userService.addUser(user);
    }

    @Test
    public void testAddNotification() {
        NotificationDto dto = new NotificationDto();
        dto.setMessage("Flight reminder");
        dto.setRead(false);
        dto.setUser(user);

        NotificationDto created = notificationService.addNotification(dto);

        assertNotNull(created.getId());
        assertEquals("Flight reminder", created.getMessage());
    }

    @Test
    public void testGetAllNotifications() {
        NotificationDto dto = new NotificationDto();
        dto.setMessage("New message");
        dto.setRead(false);
        dto.setUser(user);

        notificationService.addNotification(dto);
        List<NotificationDto> all = notificationService.getAllNotifications();

        assertFalse(all.isEmpty());
    }

    @Test
    public void testGetNotificationById() {
        NotificationDto dto = new NotificationDto();
        dto.setMessage("Boarding alert");
        dto.setRead(false);
        dto.setUser(user);

        NotificationDto created = notificationService.addNotification(dto);
        NotificationDto found = notificationService.getNotificationById(created.getId());

        assertEquals(created.getMessage(), found.getMessage());
    }

    @Test
    public void testUpdateNotification() {
        NotificationDto dto = new NotificationDto();
        dto.setMessage("Old message");
        dto.setRead(false);
        dto.setUser(user);

        NotificationDto created = notificationService.addNotification(dto);

        NotificationDto update = new NotificationDto();
        update.setMessage("Updated message");
        update.setRead(true);
        update.setUser(user);

        NotificationDto updated = notificationService.updateNotification(created.getId(), update);

        assertEquals("Updated message", updated.getMessage());
        assertTrue(updated.isRead());
    }

    @Test
    public void testDeleteNotification() {
        NotificationDto dto = new NotificationDto();
        dto.setMessage("To delete");
        dto.setRead(false);
        dto.setUser(user);

        NotificationDto created = notificationService.addNotification(dto);
        notificationService.deleteNotification(created.getId());

        assertThrows(NotificationNotFoundException.class, () -> notificationService.getNotificationById(created.getId()));
    }
}
