package com.example.cartridgeaccounting.entity.enums;

/**
 * Перечисление ролей пользователей в системе.
 * Определяет уровни доступа и права пользователей.
 * 
 * @author Система учёта картриджей
 * @version 1.0
 */
public enum UserRole {
    /**
     * Администратор системы с полными правами
     */
    ADMIN("Администратор"),
    
    /**
     * Складской работник с правами управления складом
     */
    WAREHOUSE_MANAGER("Складской работник"),
    
    /**
     * Пользователь объекта с ограниченными правами
     */
    OBJECT_USER("Пользователь объекта");

    /**
     * Описание роли на русском языке
     */
    private final String description;

    /**
     * Конструктор с описанием
     * 
     * @param description описание роли
     */
    UserRole(String description) {
        this.description = description;
    }

    /**
     * Возвращает описание роли на русском языке
     * 
     * @return описание роли
     */
    public String getDescription() {
        return description;
    }
} 