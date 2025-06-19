package com.example.cartridgeaccounting.dto;

import com.example.cartridgeaccounting.entity.enums.CartridgeStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO (Data Transfer Object) для передачи данных о картридже.
 * Используется для обмена данными между контроллерами и клиентами API.
 * 
 * @author Система учёта картриджей
 * @version 1.0
 */
@Data
public class CartridgeDto {
    
    /**
     * Уникальный идентификатор картриджа
     */
    private UUID id;
    
    /**
     * Модель картриджа
     */
    @NotBlank(message = "Модель картриджа обязательна")
    private String model;
    
    /**
     * Уникальный серийный номер картриджа
     */
    private String serialNumber;
    
    /**
     * Ресурс картриджа в страницах
     */
    @Positive(message = "Ресурс страниц должен быть положительным")
    private Integer resourcePages;
    
    /**
     * Дополнительное описание картриджа
     */
    private String description;
    
    /**
     * Текущий статус картриджа
     */
    @NotNull(message = "Статус картриджа обязателен")
    private CartridgeStatus status;
    
    /**
     * Идентификатор текущего местоположения картриджа
     */
    private UUID currentLocationId;
    
    /**
     * Название текущего местоположения картриджа
     */
    private String currentLocationName;
    
    /**
     * Дата и время создания записи
     */
    private LocalDateTime createdAt;
    
    /**
     * Дата и время последнего обновления записи
     */
    private LocalDateTime updatedAt;

    /**
     * Бренд картриджа
     */
    private String brand;

    /**
     * Артикул производителя (Part Number)
     */
    private String partNumber;

    /**
     * Цвет картриджа
     */
    private String color;

    /**
     * Совместимость с принтерами (строка)
     */
    private String compatiblePrinters;
} 