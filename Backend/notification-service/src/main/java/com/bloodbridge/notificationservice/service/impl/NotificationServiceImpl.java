package com.bloodbridge.notificationservice.service.impl;

import com.bloodbridge.notificationservice.dto.ApiResponse;
import com.bloodbridge.notificationservice.dto.NotificationDTO;
import com.bloodbridge.notificationservice.entity.Notification;
import com.bloodbridge.notificationservice.repository.NotificationRepository;
import com.bloodbridge.notificationservice.service.NotificationService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationServiceImpl(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    @Override
    public List<NotificationDTO> getAllNotifications() {
        return notificationRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public NotificationDTO getNotificationById(Long id) {
        Notification notification = notificationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + id));
        return convertToDTO(notification);
    }

    @Override
    public List<NotificationDTO> getNotificationsByUserId(Long userId) {
        return notificationRepository.findByUserId(userId).stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public NotificationDTO createNotification(NotificationDTO notificationDTO) {
        Notification notification = convertToEntity(notificationDTO);
        Notification saved = notificationRepository.save(notification);
        return convertToDTO(saved);
    }

    @Override
    public ApiResponse sendNotification(NotificationDTO notificationDTO) {
        // For prototype: just save as SENT
        Notification notification = convertToEntity(notificationDTO);
        notification.setStatus(Notification.NotificationStatus.SENT);
        notificationRepository.save(notification);
        return new ApiResponse(true, "Notification sent (simulated)");
    }

    @Override
    public ApiResponse deleteNotification(Long id) {
        if (!notificationRepository.existsById(id)) {
            return new ApiResponse(false, "Notification not found");
        }
        notificationRepository.deleteById(id);
        return new ApiResponse(true, "Notification deleted");
    }

    private NotificationDTO convertToDTO(Notification notification) {
        NotificationDTO dto = new NotificationDTO();
        BeanUtils.copyProperties(notification, dto);
        if (notification.getType() != null) dto.setType(notification.getType().name());
        if (notification.getStatus() != null) dto.setStatus(notification.getStatus().name());
        return dto;
    }

    private Notification convertToEntity(NotificationDTO dto) {
        Notification notification = new Notification();
        BeanUtils.copyProperties(dto, notification);
        if (dto.getType() != null) notification.setType(Notification.NotificationType.valueOf(dto.getType()));
        if (dto.getStatus() != null) notification.setStatus(Notification.NotificationStatus.valueOf(dto.getStatus()));
        return notification;
    }
}
