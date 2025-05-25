package com.bloodbridge.donorservice.service.impl;

import com.bloodbridge.donorservice.dto.ApiResponse;
import com.bloodbridge.donorservice.dto.DonorDTO;
import com.bloodbridge.donorservice.entity.Donor;
import com.bloodbridge.donorservice.repository.DonorRepository;
import com.bloodbridge.donorservice.service.DonorService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DonorServiceImpl implements DonorService {
    
    @Autowired
    private DonorRepository donorRepository;
    
    @Override
    public List<DonorDTO> getAllDonors() {
        List<Donor> donors = donorRepository.findAll();
        return donors.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public DonorDTO getDonorById(Long id) {
        Donor donor = donorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Donor not found with id: " + id));
        return convertToDTO(donor);
    }
    
    @Override
    public DonorDTO getDonorByUserId(Long userId) {
        Donor donor = donorRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Donor not found with userId: " + userId));
        return convertToDTO(donor);
    }
    
    @Override
    public List<DonorDTO> getDonorsByBloodType(String bloodType) {
        List<Donor> donors = donorRepository.findByBloodType(bloodType);
        return donors.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public List<DonorDTO> getPendingDonors() {
        List<Donor> donors = donorRepository.findByIsApproved(false);
        return donors.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public DonorDTO createDonor(DonorDTO donorDTO) {
        Donor donor = convertToEntity(donorDTO);
        donor.setIsApproved(false);
        Donor savedDonor = donorRepository.save(donor);
        return convertToDTO(savedDonor);
    }
    
    @Override
    public DonorDTO updateDonor(Long id, DonorDTO donorDTO) {
        Donor existingDonor = donorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Donor not found with id: " + id));
        
        // Update fields but preserve approval status and user ID
        Boolean currentApprovalStatus = existingDonor.getIsApproved();
        Long currentUserId = existingDonor.getUserId();
        
        BeanUtils.copyProperties(donorDTO, existingDonor, "id", "isApproved", "userId", "createdAt");
        
        existingDonor.setIsApproved(currentApprovalStatus);
        existingDonor.setUserId(currentUserId);
        
        Donor updatedDonor = donorRepository.save(existingDonor);
        return convertToDTO(updatedDonor);
    }
    
    @Override
    public ApiResponse approveDonor(Long id) {
        Donor donor = donorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Donor not found with id: " + id));
        
        donor.setIsApproved(true);
        donorRepository.save(donor);
        
        return new ApiResponse(true, "Donor approved successfully");
    }
    
    @Override
    public ApiResponse rejectDonor(Long id) {
        Donor donor = donorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Donor not found with id: " + id));
        
        donor.setIsApproved(false);
        donorRepository.save(donor);
        
        return new ApiResponse(true, "Donor rejected successfully");
    }
    
    @Override
    public ApiResponse deleteDonor(Long id) {
        if (!donorRepository.existsById(id)) {
            throw new RuntimeException("Donor not found with id: " + id);
        }
        
        donorRepository.deleteById(id);
        return new ApiResponse(true, "Donor deleted successfully");
    }
    
    private DonorDTO convertToDTO(Donor donor) {
        DonorDTO donorDTO = new DonorDTO();
        BeanUtils.copyProperties(donor, donorDTO);
        return donorDTO;
    }
    
    private Donor convertToEntity(DonorDTO donorDTO) {
        Donor donor = new Donor();
        BeanUtils.copyProperties(donorDTO, donor);
        return donor;
    }
}
