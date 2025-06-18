package com.example.cartridgeaccounting.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.UUID;

/**
 * DTO (Data Transfer Object) для передачи данных об объекте/месте хранения.
 * Используется для обмена данными между контроллерами и клиентами API.
 * 
 * @author Система учёта картриджей
 * @version 1.0
 */
@Data
public class LocationDto {
    
    /**
     * Уникальный идентификатор объекта
     */
    private UUID id;
    
    /**
     * Название объекта/места хранения
     */
    @NotBlank(message = "Название объекта обязательно")
    private String name;
    
    /**
     * Адрес объекта
     */
    @NotBlank(message = "Адрес объекта обязателен")
    private String address;
    
    /**
     * Контактное лицо на объекте
     */
    private String contactPerson;
    
    /**
     * Контактный телефон
     */
    private String contactPhone;
    
    /**
     * Дополнительное описание объекта
     */
    private String description;
    
    /**
     * Статус активности объекта
     */
    private boolean active;
} 