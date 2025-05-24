package com.bloodbridge.donorservice.service;

import com.bloodbridge.donorservice.dto.ApiResponse;
import com.bloodbridge.donorservice.dto.DonorDTO;

import java.util.List;

public interface DonorService {
    List<DonorDTO> getAllDonors();
    DonorDTO getDonorById(Long id);
    DonorDTO getDonorByUserId(Long userId);
    List<DonorDTO> getDonorsByBloodType(String bloodType);
    List<DonorDTO> getPendingDonors();
    DonorDTO createDonor(DonorDTO donorDTO);
    DonorDTO updateDonor(Long id, DonorDTO donorDTO);
    ApiResponse approveDonor(Long id);
    ApiResponse rejectDonor(Long id);
    ApiResponse deleteDonor(Long id);
}
