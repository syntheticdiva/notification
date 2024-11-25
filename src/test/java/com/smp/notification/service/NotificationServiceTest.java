package com.smp.notification.service;
import static org.mockito.Mockito.*;

import com.smp.notification.dto.SchoolEntityDTO;
import com.smp.notification.entity.NotificationEntity;
import com.smp.notification.repository.NotificationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testHandleSchoolNotification_RetryMechanism() {
        SchoolEntityDTO school = new SchoolEntityDTO(1L, "Test School", "123 Test St");

        // Настройка поведения репозитория на выброс исключения при первой и второй попытках.
        when(notificationRepository.save(any())).thenThrow(new RuntimeException("Database error"))
                .thenThrow(new RuntimeException("Database error"))
                .thenAnswer(invocation -> {
                    NotificationEntity notification = invocation.getArgument(0);
                    notification.setId(1L); // Успешно сохраняем на третьей попытке.
                    return notification;
                });

        notificationService.handleSchoolNotification(school);

        // Проверяем, что save был вызван 3 раза.
        verify(notificationRepository, times(3)).save(any(NotificationEntity.class));
    }
}