package com.example.cartridgeaccounting.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO для запроса создания нового объекта/места хранения.
 * Содержит данные, необходимые для создания объекта в системе.
 * 
 * @author Система учёта картриджей
 * @version 1.0
 */
@Data
public class CreateLocationRequest {
    
    /**
     * Название объекта/места хранения (обязательное поле)
     */
    @NotBlank(message = "Название объекта обязательно")
    @Size(max = 100, message = "Название объекта не должно превышать 100 символов")
    private String name;
    
    /**
     * Адрес объекта (обязательное поле)
     */
    @NotBlank(message = "Адрес объекта обязателен")
    @Size(max = 200, message = "Адрес объекта не должен превышать 200 символов")
    private String address;
    
    /**
     * Контактное лицо на объекте
     */
    @Size(max = 100, message = "Имя контактного лица не должно превышать 100 символов")
    private String contactPerson;
    
    /**
     * Контактный телефон
     */
    @Size(max = 20, message = "Номер телефона не должен превышать 20 символов")
    private String contactPhone;
    
    /**
     * Дополнительное описание объекта
     */
    @Size(max = 500, message = "Описание не должно превышать 500 символов")
    private String description;
    
    /**
     * Статус активности объекта (по умолчанию true)
     */
    private boolean active = true;
} 