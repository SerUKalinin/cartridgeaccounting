package com.example.cartridgeaccounting.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;

/**
 * DTO для запроса создания нового картриджа.
 * Содержит данные, необходимые для создания картриджа в системе.
 * 
 * @author Система учёта картриджей
 * @version 1.0
 */
@Data
public class CreateCartridgeRequest {
    
    /**
     * Модель картриджа (обязательное поле)
     */
    @NotBlank(message = "Модель картриджа обязательна")
    private String model;
    
    /**
     * Уникальный серийный номер картриджа
     */
    private String serialNumber;
    
    /**
     * Ресурс картриджа в страницах (должен быть положительным)
     */
    @Positive(message = "Ресурс страниц должен быть положительным")
    private Integer resourcePages;
    
    /**
     * Дополнительное описание картриджа
     */
    private String description;
} 