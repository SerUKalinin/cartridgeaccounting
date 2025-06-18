package com.example.cartridgeaccounting.dto;

import com.example.cartridgeaccounting.entity.enums.UserRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * DTO для запроса обновления существующего пользователя.
 * Содержит данные, которые можно изменить у пользователя.
 * Пароль является опциональным полем.
 * 
 * @author Система учёта картриджей
 * @version 1.0
 */
@Data
public class UpdateUserRequest {
    
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
     * Новый пароль пользователя (опциональное поле)
     */
    @Size(min = 6, message = "Пароль должен содержать минимум 6 символов")
    private String password;
    
    /**
     * Статус активности пользователя
     */
    private boolean enabled = true;
} 