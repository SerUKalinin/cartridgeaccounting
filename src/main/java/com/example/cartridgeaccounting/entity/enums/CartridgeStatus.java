package com.example.cartridgeaccounting.entity.enums;

/**
 * Перечисление статусов картриджа.
 * Определяет возможные состояния картриджа в системе.
 * 
 * @author Система учёта картриджей
 * @version 1.0
 */
public enum CartridgeStatus {
    /**
     * Картридж находится на складе
     */
    IN_STOCK("На складе"),
    
    /**
     * Картридж используется в МФУ
     */
    IN_USE("В использовании"),
    
    /**
     * Картридж отправлен на заправку
     */
    REFILLING("На заправке"),
    
    /**
     * Картридж списан
     */
    DISPOSED("Списан");

    /**
     * Описание статуса на русском языке
     */
    private final String description;

    /**
     * Конструктор с описанием
     * 
     * @param description описание статуса
     */
    CartridgeStatus(String description) {
        this.description = description;
    }

    /**
     * Возвращает описание статуса на русском языке
     * 
     * @return описание статуса
     */
    public String getDescription() {
        return description;
    }
} 