package com.bloodbridge.requestservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestDTO {
    private Long id;
    private Long userId;
    private String bloodType;
    private Integer unitsRequired;
    private String hospitalName;
    private String hospitalAddress;
    private String contactNumber;
    private String status; // PENDING, APPROVED, REJECTED
    private LocalDateTime requestedAt;
    private LocalDateTime fulfilledAt;
}
