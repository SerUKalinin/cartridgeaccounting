package com.example.cartridgeaccounting.exception;

import java.util.UUID;

/**
 * Исключение, возникающее при попытке найти несуществующего пользователя.
 * Выбрасывается когда пользователь с указанным ID или именем пользователя не найден в системе.
 * 
 * @author Система учёта картриджей
 * @version 1.0
 */
public class UserNotFoundException extends RuntimeException {
    
    /**
     * Конструктор с ID пользователя
     * 
     * @param id идентификатор пользователя
     */
    public UserNotFoundException(UUID id) {
        super("Пользователь с ID " + id + " не найден");
    }
    
    /**
     * Конструктор с именем пользователя
     * 
     * @param username имя пользователя
     */
    public UserNotFoundException(String username) {
        super("Пользователь с именем " + username + " не найден");
    }
} 