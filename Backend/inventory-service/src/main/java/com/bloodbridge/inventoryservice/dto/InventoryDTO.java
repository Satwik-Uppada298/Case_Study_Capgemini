package com.bloodbridge.inventoryservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryDTO {
    private Long id;
    private String bloodType;
    private Integer unitsAvailable;
    private LocalDateTime lastUpdated;
}
