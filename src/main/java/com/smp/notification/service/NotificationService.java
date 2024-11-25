package com.smp.notification.service;

import com.smp.notification.dto.SchoolEntityDTO;
import com.smp.notification.entity.NotificationEntity;
import com.smp.notification.enums.NotificationStatus;
import com.smp.notification.exception.InvalidInputException;
import com.smp.notification.exception.ProcessingException;
import com.smp.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    // Метод для обработки уведомления о создании школы
    public void handleSchoolCreationNotification(SchoolEntityDTO school) {
        // Проверка на существующее уведомление
        if (notificationRepository.existsBySchoolId(school.getId())) {
            System.out.println("Notification already exists for school ID: " + school.getId());
            return; // Если уведомление уже существует, выходим из метода
        }

        // Логика создания нового уведомления
        try {
            NotificationEntity notification = createNotification(school, "School registered successfully", NotificationStatus.SUCCESS);
            notificationRepository.save(notification);
            System.out.println("Notification saved: " + notification.getEvent());
        } catch (Exception e) {
            throw new ProcessingException("Error saving notification: " + e.getMessage());
        }
    }

    // Метод для обработки обновления уведомления о школе
    public void handleSchoolUpdateNotification(SchoolEntityDTO school) {
        // Проверка на существующее уведомление
        NotificationEntity existingNotification = notificationRepository.findBySchoolId(school.getId()).orElse(null);

        if (existingNotification != null) {
            System.out.println("Updating existing notification for school ID: " + school.getId());
            existingNotification.setEvent("School updated successfully");
            existingNotification.setStatus(NotificationStatus.SUCCESS);

            try {
                notificationRepository.save(existingNotification);
                System.out.println("Updated notification: " + existingNotification.getEvent());
            } catch (Exception e) {
                throw new ProcessingException("Error updating notification: " + e.getMessage());
            }
        } else {
            // Если уведомление не существует, создаем новое
            try {
                NotificationEntity notification = createNotification(school, "School updated successfully", NotificationStatus.SUCCESS);
                notificationRepository.save(notification);
                System.out.println("Notification saved: " + notification.getEvent());
            } catch (Exception e) {
                throw new ProcessingException("Error saving notification: " + e.getMessage());
            }
        }
    }

    // Метод для обработки общего уведомления о школе
    public void handleSchoolNotification(SchoolEntityDTO school) {
        if (school.isNew()) {
            handleSchoolCreationNotification(school);
        } else {
            handleSchoolUpdateNotification(school);
        }
    }

    // Вспомогательный метод для создания уведомления
    private NotificationEntity createNotification(SchoolEntityDTO school, String eventMessage, NotificationStatus status) {
        NotificationEntity notification = new NotificationEntity();
        notification.setSchoolId(school.getId()); // Устанавливаем ID школы
        notification.setEvent(eventMessage); // Устанавливаем сообщение события

        // Формируем полный URL
        String baseUrl = "http://localhost:8080"; // базовый URL
        String path = "/api/schools/";
        notification.setUrl(baseUrl + path + school.getId()); // полный URL

        notification.setStatus(status); // статус уведомления
        notification.setAttemptCount(0); // или другое значение по умолчанию

        return notification; // Возвращаем созданное уведомление
    }
}