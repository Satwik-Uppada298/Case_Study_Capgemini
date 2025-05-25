package com.bloodbridge.donorservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DonorDTO {
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
}
