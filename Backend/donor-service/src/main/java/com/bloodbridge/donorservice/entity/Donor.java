package com.bloodbridge.donorservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "donors")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Donor {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long userId;
    
    private String fullName;
    
    private String bloodType;
    
    private Integer age;
    
    private String gender;
    
    private String contactNumber;
    
    private String address;
    
    private String city;
    
    private String state;
    
    private String zipCode;
    
    private LocalDate lastDonationDate;
    
    private Boolean isApproved;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        if (this.isApproved == null) {
            this.isApproved = false;
        }
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
