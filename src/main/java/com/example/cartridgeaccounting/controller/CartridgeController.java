package com.example.cartridgeaccounting.controller;

import com.example.cartridgeaccounting.dto.CartridgeDto;
import com.example.cartridgeaccounting.dto.CreateCartridgeRequest;
import com.example.cartridgeaccounting.entity.enums.CartridgeStatus;
import com.example.cartridgeaccounting.service.CartridgeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST контроллер для управления картриджами.
 * Предоставляет API для создания, обновления, удаления и поиска картриджей.
 * 
 * @author Система учёта картриджей
 * @version 1.0
 */
@RestController
@RequestMapping("/api/cartridges")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Картриджи", description = "API для управления картриджами")
public class CartridgeController {
    
    private final CartridgeService cartridgeService;
    
    /**
     * Создает новый картридж
     * 
     * @param request данные для создания картриджа
     * @return созданный картридж
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE_MANAGER')")
    @Operation(summary = "Создать картридж", description = "Создает новый картридж в системе")
    public ResponseEntity<CartridgeDto> createCartridge(
            @Valid @RequestBody CreateCartridgeRequest request) {
        log.info("Запрос на создание картриджа с моделью: {}", request.getModel());
        CartridgeDto created = cartridgeService.createCartridge(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    /**
     * Получает картридж по ID
     * 
     * @param id идентификатор картриджа
     * @return картридж
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE_MANAGER') or hasRole('OBJECT_USER')")
    @Operation(summary = "Получить картридж по ID", description = "Возвращает картридж по его уникальному идентификатору")
    public ResponseEntity<CartridgeDto> getCartridgeById(
            @Parameter(description = "ID картриджа") @PathVariable UUID id) {
        log.info("Запрос на получение картриджа с ID: {}", id);
        CartridgeDto cartridge = cartridgeService.getCartridgeById(id);
        return ResponseEntity.ok(cartridge);
    }
    
    /**
     * Получает картридж по серийному номеру
     * 
     * @param serialNumber серийный номер картриджа
     * @return картридж
     */
    @GetMapping("/serial/{serialNumber}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE_MANAGER') or hasRole('OBJECT_USER')")
    @Operation(summary = "Получить картридж по серийному номеру", description = "Возвращает картридж по его серийному номеру")
    public ResponseEntity<CartridgeDto> getCartridgeBySerialNumber(
            @Parameter(description = "Серийный номер картриджа") @PathVariable String serialNumber) {
        log.info("Запрос на получение картриджа по серийному номеру: {}", serialNumber);
        CartridgeDto cartridge = cartridgeService.getCartridgeBySerialNumber(serialNumber);
        return ResponseEntity.ok(cartridge);
    }
    
    /**
     * Получает все картриджи с пагинацией
     * 
     * @param pageable параметры пагинации
     * @return страница картриджей
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE_MANAGER') or hasRole('OBJECT_USER')")
    @Operation(summary = "Получить все картриджи", description = "Возвращает список всех картриджей с пагинацией")
    public ResponseEntity<Page<CartridgeDto>> getAllCartridges(Pageable pageable) {
        log.info("Запрос на получение всех картриджей с пагинацией");
        Page<CartridgeDto> cartridges = cartridgeService.getAllCartridges(pageable);
        return ResponseEntity.ok(cartridges);
    }
    
    /**
     * Ищет картриджи по модели и серийному номеру
     * 
     * @param model модель картриджа
     * @param serialNumber серийный номер картриджа
     * @param pageable параметры пагинации
     * @return страница найденных картриджей
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE_MANAGER') or hasRole('OBJECT_USER')")
    @Operation(summary = "Поиск картриджей", description = "Ищет картриджи по модели и серийному номеру")
    public ResponseEntity<Page<CartridgeDto>> searchCartridges(
            @Parameter(description = "Модель картриджа") @RequestParam(required = false) String model,
            @Parameter(description = "Серийный номер картриджа") @RequestParam(required = false) String serialNumber,
            Pageable pageable) {
        log.info("Запрос на поиск картриджей с моделью: {}, серийным номером: {}", model, serialNumber);
        Page<CartridgeDto> cartridges = cartridgeService.searchCartridges(model, serialNumber, pageable);
        return ResponseEntity.ok(cartridges);
    }
    
    /**
     * Получает картриджи по статусу
     * 
     * @param status статус картриджа
     * @return список картриджей
     */
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE_MANAGER') or hasRole('OBJECT_USER')")
    @Operation(summary = "Получить картриджи по статусу", description = "Возвращает список картриджей с указанным статусом")
    public ResponseEntity<List<CartridgeDto>> getCartridgesByStatus(
            @Parameter(description = "Статус картриджа") @PathVariable CartridgeStatus status) {
        log.info("Запрос на получение картриджей по статусу: {}", status);
        List<CartridgeDto> cartridges = cartridgeService.getCartridgesByStatus(status);
        return ResponseEntity.ok(cartridges);
    }
    
    /**
     * Получает картриджи по объекту
     * 
     * @param locationId идентификатор объекта
     * @return список картриджей
     */
    @GetMapping("/location/{locationId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE_MANAGER') or hasRole('OBJECT_USER')")
    @Operation(summary = "Получить картриджи по объекту", description = "Возвращает список картриджей, находящихся на указанном объекте")
    public ResponseEntity<List<CartridgeDto>> getCartridgesByLocation(
            @Parameter(description = "ID объекта") @PathVariable UUID locationId) {
        log.info("Запрос на получение картриджей по объекту с ID: {}", locationId);
        List<CartridgeDto> cartridges = cartridgeService.getCartridgesByLocation(locationId);
        return ResponseEntity.ok(cartridges);
    }
    
    /**
     * Обновляет картридж
     * 
     * @param id идентификатор картриджа
     * @param request данные для обновления
     * @return обновленный картридж
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE_MANAGER')")
    @Operation(summary = "Обновить картридж", description = "Обновляет существующий картридж")
    public ResponseEntity<CartridgeDto> updateCartridge(
            @Parameter(description = "ID картриджа") @PathVariable UUID id,
            @Valid @RequestBody CreateCartridgeRequest request) {
        log.info("Запрос на обновление картриджа с ID: {}", id);
        CartridgeDto updated = cartridgeService.updateCartridge(id, request);
        return ResponseEntity.ok(updated);
    }
    
    /**
     * Удаляет картридж
     * 
     * @param id идентификатор картриджа
     * @return ответ без содержимого
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Удалить картридж", description = "Удаляет картридж из системы")
    public ResponseEntity<Void> deleteCartridge(
            @Parameter(description = "ID картриджа") @PathVariable UUID id) {
        log.info("Запрос на удаление картриджа с ID: {}", id);
        cartridgeService.deleteCartridge(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Получает количество картриджей по статусу
     * 
     * @param status статус картриджа
     * @return количество картриджей
     */
    @GetMapping("/count/status/{status}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE_MANAGER') or hasRole('OBJECT_USER')")
    @Operation(summary = "Получить количество картриджей по статусу", description = "Возвращает количество картриджей с указанным статусом")
    public ResponseEntity<Long> getCartridgeCountByStatus(
            @Parameter(description = "Статус картриджа") @PathVariable CartridgeStatus status) {
        log.info("Запрос на получение количества картриджей по статусу: {}", status);
        long count = cartridgeService.getCartridgeCountByStatus(status);
        return ResponseEntity.ok(count);
    }
    
    /**
     * Получает количество картриджей по объекту и статусу
     * 
     * @param locationId идентификатор объекта
     * @param status статус картриджа
     * @return количество картриджей
     */
    @GetMapping("/count/location/{locationId}/status/{status}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE_MANAGER') or hasRole('OBJECT_USER')")
    @Operation(summary = "Получить количество картриджей по объекту и статусу", description = "Возвращает количество картриджей с указанным статусом на указанном объекте")
    public ResponseEntity<Long> getCartridgeCountByLocationAndStatus(
            @Parameter(description = "ID объекта") @PathVariable UUID locationId,
            @Parameter(description = "Статус картриджа") @PathVariable CartridgeStatus status) {
        log.info("Запрос на получение количества картриджей по объекту {} и статусу: {}", locationId, status);
        long count = cartridgeService.getCartridgeCountByLocationAndStatus(locationId, status);
        return ResponseEntity.ok(count);
    }
} 