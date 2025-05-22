package com.travelq.service;

import com.travelq.domain.model.NotificationEntity;
import com.travelq.domain.model.UserEntity;
import com.travelq.domain.repository.NotificationRepository;
import com.travelq.domain.service.logic.NotificationServiceImpl;
import com.travelq.dto.NotificationDto;
import com.travelq.exception.NotificationNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @BeforeEach
    void setUp() {
        notificationRepository = mock(NotificationRepository.class);
        modelMapper = new ModelMapper();
        notificationService = new NotificationServiceImpl(notificationRepository, modelMapper);
    }

    @Test
    void testAddNotification() {
        NotificationDto dto = new NotificationDto(null, "Test Message", false, 1L);
        NotificationEntity entity = modelMapper.map(dto, NotificationEntity.class);
        entity.setId(1L);

        when(notificationRepository.save(any(NotificationEntity.class))).thenReturn(entity);

        NotificationDto result = notificationService.addNotification(dto);

        assertNotNull(result);
        assertEquals("Test Message", result.getMessage());
        assertEquals(1L, result.getId());
    }

    @Test
    void testGetAllNotifications() {
        NotificationEntity n1 = new NotificationEntity(1L, "Message 1", false, new UserEntity());
        NotificationEntity n2 = new NotificationEntity(2L, "Message 2", true, new UserEntity());

        when(notificationRepository.findAll()).thenReturn(Arrays.asList(n1, n2));

        List<NotificationDto> result = notificationService.getAllNotifications();

        assertEquals(2, result.size());
        assertEquals("Message 1", result.get(0).getMessage());
        assertTrue(result.get(1).isRead());
    }

    @Test
    void testGetNotificationById_Found() {
        NotificationEntity entity = new NotificationEntity(1L, "Message", false, new UserEntity());

        when(notificationRepository.findById(1L)).thenReturn(Optional.of(entity));

        NotificationDto result = notificationService.getNotificationById(1L);

        assertNotNull(result);
        assertEquals("Message", result.getMessage());
    }

    @Test
    void testGetNotificationById_NotFound() {
        when(notificationRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotificationNotFoundException.class, () -> notificationService.getNotificationById(1L));
    }

    @Test
    void testUpdateNotification_Found() {
        NotificationEntity existing = new NotificationEntity(1L, "Old Message", false, new UserEntity());
        NotificationDto updatedDto = new NotificationDto(1L, "New Message", true, 1L);

        when(notificationRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(notificationRepository.save(any(NotificationEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        NotificationDto result = notificationService.updateNotification(1L, updatedDto);

        assertEquals("New Message", result.getMessage());
        assertTrue(result.isRead());
    }

    @Test
    void testUpdateNotification_NotFound() {
        NotificationDto dto = new NotificationDto(1L, "Update", true, 1L);

        when(notificationRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(NotificationNotFoundException.class, () -> notificationService.updateNotification(1L, dto));
    }

    @Test
    void testDeleteNotification_Found() {
        when(notificationRepository.existsById(1L)).thenReturn(true);

        notificationService.deleteNotification(1L);

        verify(notificationRepository).deleteById(1L);
    }

    @Test
    void testDeleteNotification_NotFound() {
        when(notificationRepository.existsById(1L)).thenReturn(false);

        assertThrows(NotificationNotFoundException.class, () -> notificationService.deleteNotification(1L));
    }
}

