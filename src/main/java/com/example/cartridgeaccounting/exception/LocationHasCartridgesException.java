package com.example.cartridgeaccounting.exception;

import java.util.UUID;

/**
 * Исключение, возникающее при попытке удалить объект, который имеет связанные картриджи.
 * 
 * @author Система учёта картриджей
 * @version 1.0
 */
public class LocationHasCartridgesException extends RuntimeException {
    
    private final UUID locationId;
    private final String locationName;
    private final long cartridgeCount;
    
    /**
     * Конструктор с ID объекта, названием и количеством связанных картриджей
     * 
     * @param locationId ID объекта
     * @param locationName название объекта
     * @param cartridgeCount количество связанных картриджей
     */
    public LocationHasCartridgesException(UUID locationId, String locationName, long cartridgeCount) {
        super(String.format("Невозможно удалить объект \"%s\", так как он имеет %d связанных картриджей. " +
                "Сначала переместите или удалите все картриджи с этого объекта.", locationName, cartridgeCount));
        this.locationId = locationId;
        this.locationName = locationName;
        this.cartridgeCount = cartridgeCount;
    }
    
    /**
     * Возвращает ID объекта
     * 
     * @return ID объекта
     */
    public UUID getLocationId() {
        return locationId;
    }
    
    /**
     * Возвращает название объекта
     * 
     * @return название объекта
     */
    public String getLocationName() {
        return locationName;
    }
    
    /**
     * Возвращает количество связанных картриджей
     * 
     * @return количество связанных картриджей
     */
    public long getCartridgeCount() {
        return cartridgeCount;
    }
} 