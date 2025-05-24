package com.bloodbridge.notificationservice.controller;

import com.bloodbridge.notificationservice.dto.ApiResponse;
import com.bloodbridge.notificationservice.dto.NotificationDTO;
import com.bloodbridge.notificationservice.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "*")
public class NotificationController {
    @Autowired
    private NotificationService notificationService;

    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getAllNotifications() {
        return ResponseEntity.ok(notificationService.getAllNotifications());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationDTO> getNotificationById(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.getNotificationById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<NotificationDTO>> getNotificationsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(notificationService.getNotificationsByUserId(userId));
    }

    @PostMapping
    public ResponseEntity<NotificationDTO> createNotification(@RequestBody NotificationDTO notificationDTO) {
        return new ResponseEntity<>(notificationService.createNotification(notificationDTO), HttpStatus.CREATED);
    }

    @PostMapping("/send")
    public ResponseEntity<ApiResponse> sendNotification(@RequestBody NotificationDTO notificationDTO) {
        return ResponseEntity.ok(notificationService.sendNotification(notificationDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteNotification(@PathVariable Long id) {
        return ResponseEntity.ok(notificationService.deleteNotification(id));
    }
}
