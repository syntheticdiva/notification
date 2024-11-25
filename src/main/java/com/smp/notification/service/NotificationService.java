package com.smp.notification.service;

import com.smp.notification.dto.SchoolEntityDTO;
import com.smp.notification.entity.NotificationEntity;
import com.smp.notification.enums.NotificationStatus;
import com.smp.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    // Обработка уведомления о создании или обновлении школы
    public void handleSchoolNotification(SchoolEntityDTO school) {
        String eventMessage = school.isNew() ? "School registered successfully" : "School updated successfully";
        NotificationStatus status = NotificationStatus.SUCCESS;

        try {
            NotificationEntity notification = createNotification(school, eventMessage, status);
            notificationRepository.save(notification);
            System.out.println("Notification saved: " + notification.getEvent());
        } catch (Exception e) {
            System.out.println("Error saving notification: " + e.getMessage());

        }
    }

    private NotificationEntity createNotification(SchoolEntityDTO school, String eventMessage, NotificationStatus status) {
        NotificationEntity notification = new NotificationEntity();
        notification.setSchoolId(school.getId()); // Устанавливаем ID школы
        notification.setEvent(eventMessage); // Устанавливаем сообщение события

        // Формируем полный URL
        String baseUrl = "http://localhost:8080"; //  базовый URL
        String path = "/api/schools/";
        notification.setUrl(baseUrl + path + school.getId()); // полный URL

        notification.setStatus(status); // статус уведомления
        notification.setAttemptCount(0); // или другое значение

        return notification; // Возвращаем созданное уведомление
    }
}
