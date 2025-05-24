package com.bloodbridge.inventoryservice.controller;

import com.bloodbridge.inventoryservice.dto.ApiResponse;
import com.bloodbridge.inventoryservice.dto.InventoryDTO;
import com.bloodbridge.inventoryservice.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@CrossOrigin(origins = "*")
public class InventoryController {
    
    @Autowired
    private InventoryService inventoryService;
    
    @GetMapping
    public ResponseEntity<List<InventoryDTO>> getAllInventory() {
        return ResponseEntity.ok(inventoryService.getAllInventory());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<InventoryDTO> getInventoryById(@PathVariable Long id) {
        return ResponseEntity.ok(inventoryService.getInventoryById(id));
    }
    
    @GetMapping("/blood-type/{bloodType}")
    public ResponseEntity<InventoryDTO> getInventoryByBloodType(@PathVariable String bloodType) {
        return ResponseEntity.ok(inventoryService.getInventoryByBloodType(bloodType));
    }
    
    @PostMapping
    public ResponseEntity<InventoryDTO> createInventory(@RequestBody InventoryDTO inventoryDTO) {
        return new ResponseEntity<>(inventoryService.createInventory(inventoryDTO), HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<InventoryDTO> updateInventory(@PathVariable Long id, @RequestBody InventoryDTO inventoryDTO) {
        return ResponseEntity.ok(inventoryService.updateInventory(id, inventoryDTO));
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteInventory(@PathVariable Long id) {
        return ResponseEntity.ok(inventoryService.deleteInventory(id));
    }
    
    @PutMapping("/blood-type/{bloodType}/add/{units}")
    public ResponseEntity<ApiResponse> addUnits(@PathVariable String bloodType, @PathVariable Integer units) {
        return ResponseEntity.ok(inventoryService.updateUnits(bloodType, units, true));
    }
    
    @PutMapping("/blood-type/{bloodType}/deduct/{units}")
    public ResponseEntity<ApiResponse> deductUnits(@PathVariable String bloodType, @PathVariable Integer units) {
        return ResponseEntity.ok(inventoryService.updateUnits(bloodType, units, false));
    }
    
    @GetMapping("/check/{bloodType}/{units}")
    public ResponseEntity<Boolean> checkAvailability(@PathVariable String bloodType, @PathVariable Integer units) {
        return ResponseEntity.ok(inventoryService.checkAvailability(bloodType, units));
    }
}
