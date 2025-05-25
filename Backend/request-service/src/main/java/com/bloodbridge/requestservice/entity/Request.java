package com.bloodbridge.requestservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Request {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long userId;
    
    private String bloodType;
    
    private Integer unitsRequired;
    
    private String hospitalName;
    
    private String hospitalAddress;
    
    private String contactNumber;
    
    @Enumerated(EnumType.STRING)
    private RequestStatus status;
    
    private LocalDateTime requestedAt;
    
    private LocalDateTime fulfilledAt;
    
    @PrePersist
    protected void onCreate() {
        this.requestedAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = RequestStatus.PENDING;
        }
    }
    
    public enum RequestStatus {
        PENDING, APPROVED, REJECTED
    }
}
