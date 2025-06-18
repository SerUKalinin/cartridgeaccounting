package com.example.cartridgeaccounting.dto;

import com.example.cartridgeaccounting.entity.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO для запроса создания нового пользователя.
 * Содержит данные, необходимые для создания пользователя в системе.
 * 
 * @author Система учёта картриджей
 * @version 1.0
 */
@Data
public class CreateUserRequest {
    
    /**
     * Уникальное имя пользователя для входа в систему (обязательное поле)
     */
    @NotBlank(message = "Имя пользователя обязательно")
    @Size(min = 3, max = 50, message = "Имя пользователя должно быть от 3 до 50 символов")
    private String username;
    
    /**
     * Пароль пользователя (обязательное поле)
     */
    @NotBlank(message = "Пароль обязателен")
    @Size(min = 6, message = "Пароль должен содержать минимум 6 символов")
    private String password;
    
    /**
     * Полное имя пользователя (обязательное поле)
     */
    @NotBlank(message = "Полное имя обязательно")
    @Size(max = 100, message = "Полное имя не должно превышать 100 символов")
    private String fullName;
    
    /**
     * Роль пользователя в системе (обязательное поле)
     */
    @NotNull(message = "Роль пользователя обязательна")
    private UserRole role;
    
    /**
     * Статус активности пользователя (по умолчанию true)
     */
    private boolean enabled = true;
} 