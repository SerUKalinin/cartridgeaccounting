package com.example.cartridgeaccounting.exception;

/**
 * Исключение, возникающее при попытке создать картридж с уже существующим серийным номером.
 * Выбрасывается когда в системе уже есть картридж с указанным серийным номером.
 * 
 * @author Система учёта картриджей
 * @version 1.0
 */
public class DuplicateSerialNumberException extends RuntimeException {
    
    /**
     * Конструктор с серийным номером
     * 
     * @param serialNumber серийный номер картриджа
     */
    public DuplicateSerialNumberException(String serialNumber) {
        super("Картридж с серийным номером " + serialNumber + " уже существует");
    }
    
    public DuplicateSerialNumberException(String serialNumber, String message) {
        super("Картридж с серийным номером " + serialNumber + " уже существует: " + message);
    }
} 