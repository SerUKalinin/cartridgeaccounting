package com.example.cartridgeaccounting.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.UUID;

/**
 * Сущность объекта/места хранения картриджей.
 * Представляет физическое место, где могут храниться или использоваться картриджи.
 * 
 * @author Система учёта картриджей
 * @version 1.0
 */
@Entity
@Table(name = "locations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {
    
    /**
     * Уникальный идентификатор объекта
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    /**
     * Название объекта/места хранения
     */
    @Column(nullable = false)
    private String name;
    
    /**
     * Адрес объекта
     */
    @Column(nullable = false)
    private String address;
    
    /**
     * Контактное лицо на объекте
     */
    @Column(name = "contact_person")
    private String contactPerson;
    
    /**
     * Контактный телефон
     */
    @Column(name = "contact_phone")
    private String contactPhone;
    
    /**
     * Дополнительное описание объекта
     */
    @Column
    private String description;
    
    /**
     * Статус активности объекта
     */
    @Column(nullable = false)
    private boolean active = true;
} 