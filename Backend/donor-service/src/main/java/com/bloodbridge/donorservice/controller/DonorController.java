package com.bloodbridge.donorservice.controller;

import com.bloodbridge.donorservice.dto.ApiResponse;
import com.bloodbridge.donorservice.dto.DonorDTO;
import com.bloodbridge.donorservice.service.DonorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/donors")
@CrossOrigin(origins = "*")
public class DonorController {
    private final DonorService donorService;

    public DonorController(DonorService donorService) {
        this.donorService = donorService;
    }


    @GetMapping
    public ResponseEntity<List<DonorDTO>> getAllDonors() {
        return ResponseEntity.ok(donorService.getAllDonors());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DonorDTO> getDonorById(@PathVariable Long id) {
        return ResponseEntity.ok(donorService.getDonorById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<DonorDTO> getDonorByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(donorService.getDonorByUserId(userId));
    }

    @GetMapping("/blood-type/{bloodType}")
    public ResponseEntity<List<DonorDTO>> getDonorsByBloodType(@PathVariable String bloodType) {
        return ResponseEntity.ok(donorService.getDonorsByBloodType(bloodType));
    }

    @GetMapping("/pending")
    public ResponseEntity<List<DonorDTO>> getPendingDonors() {
        return ResponseEntity.ok(donorService.getPendingDonors());
    }

    @PostMapping
    public ResponseEntity<DonorDTO> createDonor(@RequestBody DonorDTO donorDTO) {
        return new ResponseEntity<>(donorService.createDonor(donorDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<DonorDTO> updateDonor(@PathVariable Long id, @RequestBody DonorDTO donorDTO) {
        return ResponseEntity.ok(donorService.updateDonor(id, donorDTO));
    }

    @PutMapping("/{id}/approve")
    public ResponseEntity<ApiResponse> approveDonor(@PathVariable Long id) {
        return ResponseEntity.ok(donorService.approveDonor(id));
    }

    @PutMapping("/{id}/reject")
    public ResponseEntity<ApiResponse> rejectDonor(@PathVariable Long id) {
        return ResponseEntity.ok(donorService.rejectDonor(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteDonor(@PathVariable Long id) {
        return ResponseEntity.ok(donorService.deleteDonor(id));
    }
}
