package com.example.cartridgeaccounting.entity;

import com.example.cartridgeaccounting.entity.enums.CartridgeStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Сущность картриджа для МФУ.
 * Представляет физический картридж с его характеристиками и текущим состоянием.
 * 
 * @author Система учёта картриджей
 * @version 1.0
 */
@Entity
@Table(name = "cartridges")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cartridge {
    
    /**
     * Уникальный идентификатор картриджа
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    /**
     * Модель картриджа
     */
    @Column(nullable = false)
    private String model;
    
    /**
     * Уникальный серийный номер картриджа
     */
    @Column(name = "serial_number", unique = true)
    private String serialNumber;
    
    /**
     * Ресурс картриджа в страницах
     */
    @Column(name = "resource_pages")
    private Integer resourcePages;
    
    /**
     * Дополнительное описание картриджа
     */
    @Column
    private String description;
    
    /**
     * Текущий статус картриджа
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CartridgeStatus status = CartridgeStatus.IN_STOCK;
    
    /**
     * Текущее местоположение картриджа
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "current_location_id")
    private Location currentLocation;
    
    /**
     * Дата и время создания записи
     */
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
    
    /**
     * Дата и время последнего обновления записи
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    /**
     * Автоматическое обновление времени изменения записи
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * Бренд картриджа
     */
    @Column(name = "brand")
    private String brand;

    /**
     * Артикул производителя (Part Number)
     */
    @Column(name = "part_number")
    private String partNumber;

    /**
     * Цвет картриджа
     */
    @Column(name = "color")
    private String color;

    /**
     * Совместимость с принтерами (строка)
     */
    @Column(name = "compatible_printers")
    private String compatiblePrinters;
} 