package com.bloodbridge.requestservice.controller;

import com.bloodbridge.requestservice.dto.ApiResponse;
import com.bloodbridge.requestservice.dto.RequestDTO;
import com.bloodbridge.requestservice.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
@CrossOrigin(origins = "*")
public class RequestController {
    
    @Autowired
    private RequestService requestService;
    
    @GetMapping
    public ResponseEntity<List<RequestDTO>> getAllRequests() {
        return ResponseEntity.ok(requestService.getAllRequests());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<RequestDTO> getRequestById(@PathVariable Long id) {
        return ResponseEntity.ok(requestService.getRequestById(id));
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RequestDTO>> getRequestsByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(requestService.getRequestsByUserId(userId));
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<RequestDTO>> getRequestsByStatus(@PathVariable String status) {
        return ResponseEntity.ok(requestService.getRequestsByStatus(status));
    }
    
    @GetMapping("/blood-type/{bloodType}")
    public ResponseEntity<List<RequestDTO>> getRequestsByBloodType(@PathVariable String bloodType) {
        return ResponseEntity.ok(requestService.getRequestsByBloodType(bloodType));
    }
    
    @PostMapping
    public ResponseEntity<RequestDTO> createRequest(@RequestBody RequestDTO requestDTO) {
        return new ResponseEntity<>(requestService.createRequest(requestDTO), HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<RequestDTO> updateRequest(@PathVariable Long id, @RequestBody RequestDTO requestDTO) {
        return ResponseEntity.ok(requestService.updateRequest(id, requestDTO));
    }
    
    @PutMapping("/{id}/approve")
    public ResponseEntity<ApiResponse> approveRequest(@PathVariable Long id) {
        return ResponseEntity.ok(requestService.approveRequest(id));
    }
    
    @PutMapping("/{id}/reject")
    public ResponseEntity<ApiResponse> rejectRequest(@PathVariable Long id) {
        return ResponseEntity.ok(requestService.rejectRequest(id));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteRequest(@PathVariable Long id) {
        return ResponseEntity.ok(requestService.deleteRequest(id));
    }
}
