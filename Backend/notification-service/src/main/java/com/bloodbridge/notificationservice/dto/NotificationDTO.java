package com.bloodbridge.notificationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    private Long id;
    private Long userId;
    private String message;
    private String type; // EMAIL, SMS
    private String status; // SENT, FAILED
    private LocalDateTime createdAt;
}
