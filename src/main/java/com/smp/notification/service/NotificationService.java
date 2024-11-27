package com.smp.notification.service;

import com.smp.notification.dto.SchoolEntityDTO;
import com.smp.notification.entity.NotificationEntity;
import com.smp.notification.enums.NotificationStatus;
import com.smp.notification.exception.NotificationNotFoundException;
import com.smp.notification.exception.ProcessingException;
import com.smp.notification.repository.NotificationRepository;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void handleSchoolCreationNotification(SchoolEntityDTO school) {
        try {
            if (notificationRepository.existsBySchoolId(school.getId())) {
                System.out.println("Notification already exists for school ID: " + school.getId());
                return;
            }

            NotificationEntity notification = createNotification(school, "School registered successfully", NotificationStatus.SUCCESS);
            notificationRepository.save(notification);
            System.out.println("Notification saved: " + notification.getEvent());
        } catch (Exception e) {
            NotificationEntity failedNotification = createNotification(school,
                    "School registration failed: " + e.getMessage(),
                    NotificationStatus.FAILED);
            notificationRepository.save(failedNotification);

            throw new ProcessingException("Error saving notification: " + e.getMessage());
        }
    }

    public void handleSchoolUpdateNotification(SchoolEntityDTO school) {
        try {
            NotificationEntity existingNotification = notificationRepository.findBySchoolId(school.getId()).orElse(null);

            if (existingNotification != null) {
                System.out.println("Updating existing notification for school ID: " + school.getId());
                existingNotification.setEvent("School updated successfully");
                existingNotification.setStatus(NotificationStatus.SUCCESS);

                notificationRepository.save(existingNotification);
                System.out.println("Updated notification: " + existingNotification.getEvent());
            } else {
                throw new NotificationNotFoundException("Уведомление с ID " + school.getId() + " не существует");
            }
        } catch (Exception e) {
            NotificationEntity failedNotification = createNotification(school,
                    "School update failed: " + e.getMessage(),
                    NotificationStatus.FAILED);
            notificationRepository.save(failedNotification);

            throw new ProcessingException("Error updating notification: " + e.getMessage());
        }
    }

    public void handleSchoolNotification(SchoolEntityDTO school) {
        try {
            if (school.isNew()) {
                handleSchoolCreationNotification(school);
            } else {
                handleSchoolUpdateNotification(school);
            }
        } catch (Exception e) {
            NotificationEntity failedNotification = createNotification(school,
                    "School notification failed: " + e.getMessage(),
                    NotificationStatus.FAILED);
            notificationRepository.save(failedNotification);

            throw e;
        }
    }

    private NotificationEntity createNotification(SchoolEntityDTO school, String eventMessage, NotificationStatus status) {
        NotificationEntity notification = new NotificationEntity();
        notification.setSchoolId(school.getId());
        notification.setEvent(eventMessage);

        String baseUrl = "http://localhost:8080";
        String path = "/api/schools/";
        notification.setUrl(baseUrl + path + school.getId());

        notification.setStatus(status);
        notification.setAttemptCount(0);

        return notification;
    }
}