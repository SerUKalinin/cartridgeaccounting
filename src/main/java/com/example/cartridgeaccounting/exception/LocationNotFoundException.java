package com.example.cartridgeaccounting.exception;

import java.util.UUID;

/**
 * Исключение, возникающее при попытке найти несуществующий объект/место хранения.
 * Выбрасывается когда объект с указанным ID или названием не найден в системе.
 * 
 * @author Система учёта картриджей
 * @version 1.0
 */
public class LocationNotFoundException extends RuntimeException {
    
    /**
     * Конструктор с ID объекта
     * 
     * @param id идентификатор объекта
     */
    public LocationNotFoundException(UUID id) {
        super("Объект с ID " + id + " не найден");
    }
    
    /**
     * Конструктор с названием объекта
     * 
     * @param name название объекта
     */
    public LocationNotFoundException(String name) {
        super("Объект с названием " + name + " не найден");
    }
} 