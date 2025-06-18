package com.example.cartridgeaccounting.dto;

import com.example.cartridgeaccounting.entity.enums.OperationType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class OperationDto {
    
    private UUID id;
    
    @NotNull(message = "Тип операции обязателен")
    private OperationType type;
    
    @NotNull(message = "Количество обязательно")
    @Positive(message = "Количество должно быть положительным")
    private Integer count;
    
    @NotNull(message = "ID картриджа обязателен")
    private UUID cartridgeId;
    private String cartridgeModel;
    private String cartridgeSerialNumber;
    
    private UUID locationId;
    private String locationName;
    
    private UUID performedById;
    private String performedByUsername;
    
    private LocalDateTime operationDate;
    private String notes;
} 