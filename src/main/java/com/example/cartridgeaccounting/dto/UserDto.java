package com.example.cartridgeaccounting.dto;

import com.example.cartridgeaccounting.entity.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

/**
 * DTO (Data Transfer Object) для передачи данных о пользователе.
 * Используется для обмена данными между контроллерами и клиентами API.
 * Не содержит пароль для безопасности.
 * 
 * @author Система учёта картриджей
 * @version 1.0
 */
@Data
public class UserDto {
    
    /**
     * Уникальный идентификатор пользователя
     */
    private UUID id;
    
    /**
     * Уникальное имя пользователя для входа в систему
     */
    @NotBlank(message = "Имя пользователя обязательно")
    private String username;
    
    /**
     * Полное имя пользователя
     */
    @NotBlank(message = "Полное имя обязательно")
    private String fullName;
    
    /**
     * Роль пользователя в системе
     */
    @NotNull(message = "Роль пользователя обязательна")
    private UserRole role;
    
    /**
     * Статус активности пользователя
     */
    private boolean enabled;
} 