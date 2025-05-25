package com.bloodbridge.notificationservice.service;

import com.bloodbridge.notificationservice.dto.ApiResponse;
import com.bloodbridge.notificationservice.dto.NotificationDTO;

import java.util.List;

public interface NotificationService {
    List<NotificationDTO> getAllNotifications();
    NotificationDTO getNotificationById(Long id);
    List<NotificationDTO> getNotificationsByUserId(Long userId);
    NotificationDTO createNotification(NotificationDTO notificationDTO);
    ApiResponse sendNotification(NotificationDTO notificationDTO);
    ApiResponse deleteNotification(Long id);
}
