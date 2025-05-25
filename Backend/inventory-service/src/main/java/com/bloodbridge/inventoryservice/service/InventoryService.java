package com.bloodbridge.inventoryservice.service;

import com.bloodbridge.inventoryservice.dto.ApiResponse;
import com.bloodbridge.inventoryservice.dto.InventoryDTO;

import java.util.List;

public interface InventoryService {
    List<InventoryDTO> getAllInventory();
    InventoryDTO getInventoryById(Long id);
    InventoryDTO getInventoryByBloodType(String bloodType);
    InventoryDTO createInventory(InventoryDTO inventoryDTO);
    InventoryDTO updateInventory(Long id, InventoryDTO inventoryDTO);
    ApiResponse deleteInventory(Long id);
    ApiResponse updateUnits(String bloodType, Integer units, boolean isAddition);
    boolean checkAvailability(String bloodType, Integer unitsRequired);
}
