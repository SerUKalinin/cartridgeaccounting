package com.example.cartridgeaccounting.exception;

import java.util.UUID;

/**
 * Исключение, возникающее при попытке найти несуществующий картридж.
 * Выбрасывается когда картридж с указанным ID или серийным номером не найден в системе.
 * 
 * @author Система учёта картриджей
 * @version 1.0
 */
public class CartridgeNotFoundException extends RuntimeException {
    
    /**
     * Конструктор с ID картриджа
     * 
     * @param id идентификатор картриджа
     */
    public CartridgeNotFoundException(UUID id) {
        super("Картридж с ID " + id + " не найден");
    }
    
    /**
     * Конструктор с серийным номером и дополнительным сообщением
     * 
     * @param serialNumber серийный номер картриджа
     * @param message дополнительное сообщение
     */
    public CartridgeNotFoundException(String serialNumber, String message) {
        super("Картридж с серийным номером " + serialNumber + " " + message);
    }
    
    /**
     * Конструктор с серийным номером
     * 
     * @param serialNumber серийный номер картриджа
     */
    public CartridgeNotFoundException(String serialNumber) {
        super("Картридж с серийным номером " + serialNumber + " не найден");
    }
} 