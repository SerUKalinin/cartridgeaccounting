package com.example.cartridgeaccounting.entity;

import com.example.cartridgeaccounting.entity.enums.OperationType;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Сущность операции с картриджем.
 * Представляет любое действие, выполняемое с картриджем (поступление, выдача, возврат и т.д.).
 * 
 * @author Система учёта картриджей
 * @version 1.0
 */
@Entity
@Table(name = "operations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Operation {
    
    /**
     * Уникальный идентификатор операции
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    /**
     * Тип выполняемой операции
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OperationType type;
    
    /**
     * Количество картриджей в операции
     */
    @Column(nullable = false)
    private Integer count;
    
    /**
     * Картридж, с которым выполняется операция
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cartridge_id", nullable = false)
    private Cartridge cartridge;
    
    /**
     * Объект, связанный с операцией (место назначения или источник)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private Location location;
    
    /**
     * Пользователь, выполнивший операцию
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "performed_by", nullable = false)
    private User performedBy;
    
    /**
     * Дата и время выполнения операции
     */
    @Column(name = "operation_date", nullable = false)
    private LocalDateTime operationDate = LocalDateTime.now();
    
    /**
     * Дополнительные заметки к операции
     */
    @Column
    private String notes;
} 