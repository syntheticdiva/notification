package com.smp.notification.controller;
import com.smp.notification.dto.SchoolEntityDTO;
import com.smp.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping
    public ResponseEntity<String> handleSchoolNotification(@RequestBody SchoolEntityDTO school) {
        notificationService.handleSchoolNotification(school);
        return ResponseEntity.ok("Notification processed successfully");
    }
}