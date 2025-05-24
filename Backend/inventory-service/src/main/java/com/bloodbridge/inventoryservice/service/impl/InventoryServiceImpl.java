package com.bloodbridge.inventoryservice.service.impl;

import com.bloodbridge.inventoryservice.dto.ApiResponse;
import com.bloodbridge.inventoryservice.dto.InventoryDTO;
import com.bloodbridge.inventoryservice.entity.Inventory;
import com.bloodbridge.inventoryservice.repository.InventoryRepository;
import com.bloodbridge.inventoryservice.service.InventoryService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryServiceImpl implements InventoryService {
    
    @Autowired
    private InventoryRepository inventoryRepository;
    
    @Override
    public List<InventoryDTO> getAllInventory() {
        List<Inventory> inventoryList = inventoryRepository.findAll();
        return inventoryList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    @Override
    public InventoryDTO getInventoryById(Long id) {
        Inventory inventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found with id: " + id));
        return convertToDTO(inventory);
    }
    
    @Override
    public InventoryDTO getInventoryByBloodType(String bloodType) {
        Inventory inventory = inventoryRepository.findByBloodType(bloodType)
                .orElseThrow(() -> new RuntimeException("Inventory not found for blood type: " + bloodType));
        return convertToDTO(inventory);
    }
    
    @Override
    public InventoryDTO createInventory(InventoryDTO inventoryDTO) {
        if (inventoryRepository.findByBloodType(inventoryDTO.getBloodType()).isPresent()) {
            throw new RuntimeException("Inventory already exists for blood type: " + inventoryDTO.getBloodType());
        }
        
        Inventory inventory = convertToEntity(inventoryDTO);
        Inventory savedInventory = inventoryRepository.save(inventory);
        return convertToDTO(savedInventory);
    }
    
    @Override
    public InventoryDTO updateInventory(Long id, InventoryDTO inventoryDTO) {
        Inventory existingInventory = inventoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventory not found with id: " + id));
        
        if (!existingInventory.getBloodType().equals(inventoryDTO.getBloodType()) 
                && inventoryRepository.findByBloodType(inventoryDTO.getBloodType()).isPresent()) {
            throw new RuntimeException("Another inventory already exists for blood type: " + inventoryDTO.getBloodType());
        }
        
        BeanUtils.copyProperties(inventoryDTO, existingInventory, "id", "lastUpdated");
        Inventory updatedInventory = inventoryRepository.save(existingInventory);
        return convertToDTO(updatedInventory);
    }
    
    @Override
    public ApiResponse deleteInventory(Long id) {
        if (!inventoryRepository.existsById(id)) {
            throw new RuntimeException("Inventory not found with id: " + id);
        }
        
        inventoryRepository.deleteById(id);
        return new ApiResponse(true, "Inventory deleted successfully");
    }
    
    @Override
    public ApiResponse updateUnits(String bloodType, Integer units, boolean isAddition) {
        Inventory inventory = inventoryRepository.findByBloodType(bloodType)
                .orElseGet(() -> {
                    Inventory newInventory = new Inventory();
                    newInventory.setBloodType(bloodType);
                    newInventory.setUnitsAvailable(0);
                    return newInventory;
                });
        
        if (isAddition) {
            inventory.setUnitsAvailable(inventory.getUnitsAvailable() + units);
        } else {
            if (inventory.getUnitsAvailable() < units) {
                return new ApiResponse(false, "Not enough units available for blood type: " + bloodType);
            }
            inventory.setUnitsAvailable(inventory.getUnitsAvailable() - units);
        }
        
        inventoryRepository.save(inventory);
        return new ApiResponse(true, "Units " + (isAddition ? "added to" : "deducted from") + " inventory successfully");
    }
    
    @Override
    public boolean checkAvailability(String bloodType, Integer unitsRequired) {
        return inventoryRepository.findByBloodType(bloodType)
                .map(inventory -> inventory.getUnitsAvailable() >= unitsRequired)
                .orElse(false);
    }
    
    private InventoryDTO convertToDTO(Inventory inventory) {
        InventoryDTO inventoryDTO = new InventoryDTO();
        BeanUtils.copyProperties(inventory, inventoryDTO);
        return inventoryDTO;
    }
    
    private Inventory convertToEntity(InventoryDTO inventoryDTO) {
        Inventory inventory = new Inventory();
        BeanUtils.copyProperties(inventoryDTO, inventory);
        return inventory;
    }
}
