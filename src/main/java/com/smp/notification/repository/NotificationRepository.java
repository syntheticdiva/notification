package com.smp.notification.repository;
import com.smp.notification.entity.NotificationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NotificationRepository extends JpaRepository<NotificationEntity, Long> {
    boolean existsBySchoolId(Long schoolId);
        Optional<NotificationEntity> findBySchoolId(Long schoolId);
    }

