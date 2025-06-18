package com.example.cartridgeaccounting.dto;

import com.example.cartridgeaccounting.entity.enums.OperationType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateOperationRequest {
    
    @NotNull(message = "Тип операции обязателен")
    private OperationType type;
    
    @NotNull(message = "Количество обязательно")
    @Positive(message = "Количество должно быть положительным")
    private Integer count;
    
    @NotNull(message = "ID картриджа обязателен")
    private UUID cartridgeId;
    
    private UUID locationId;
    private String notes;
} 