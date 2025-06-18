package com.example.cartridgeaccounting.entity.enums;

/**
 * Перечисление типов операций с картриджами.
 * Определяет возможные действия, которые можно выполнить с картриджем.
 * 
 * @author Система учёта картриджей
 * @version 1.0
 */
public enum OperationType {
    /**
     * Поступление картриджа на склад
     */
    RECEIPT("Поступление"),
    
    /**
     * Выдача картриджа на объект
     */
    ISSUE("Выдача"),
    
    /**
     * Возврат картриджа на склад
     */
    RETURN("Возврат"),
    
    /**
     * Отправка картриджа на заправку
     */
    REFILL("Заправка"),
    
    /**
     * Списание картриджа
     */
    DISPOSAL("Списание");

    /**
     * Описание типа операции на русском языке
     */
    private final String description;

    /**
     * Конструктор с описанием
     * 
     * @param description описание типа операции
     */
    OperationType(String description) {
        this.description = description;
    }

    /**
     * Возвращает описание типа операции на русском языке
     * 
     * @return описание типа операции
     */
    public String getDescription() {
        return description;
    }
} 