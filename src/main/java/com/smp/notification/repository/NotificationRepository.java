package com.smp.notification.repository;
import com.smp.notification.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    List<NotificationEntity> findByStatus(String status);
    boolean existsBySchoolId(Long schoolId);
        // Метод для поиска уведомления по ID школы
        Optional<NotificationEntity> findBySchoolId(Long schoolId);
    }

