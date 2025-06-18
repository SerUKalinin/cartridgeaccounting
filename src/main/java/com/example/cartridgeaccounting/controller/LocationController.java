package com.example.cartridgeaccounting.controller;

import com.example.cartridgeaccounting.dto.CreateLocationRequest;
import com.example.cartridgeaccounting.dto.LocationDto;
import com.example.cartridgeaccounting.service.LocationService;
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
 * REST контроллер для управления объектами/местами хранения.
 * Предоставляет API для создания, обновления, удаления и поиска объектов.
 * 
 * @author Система учёта картриджей
 * @version 1.0
 */
@RestController
@RequestMapping("/api/locations")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Объекты", description = "API для управления объектами/местами хранения")
public class LocationController {
    
    private final LocationService locationService;
    
    /**
     * Создает новый объект
     * 
     * @param request данные для создания объекта
     * @return созданный объект
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE_MANAGER')")
    @Operation(summary = "Создать объект", description = "Создает новый объект/место хранения в системе")
    public ResponseEntity<LocationDto> createLocation(@Valid @RequestBody CreateLocationRequest request) {
        log.info("Запрос на создание объекта с названием: {}", request.getName());
        LocationDto created = locationService.createLocation(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    /**
     * Получает объект по ID
     * 
     * @param id идентификатор объекта
     * @return объект
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE_MANAGER') or hasRole('OBJECT_USER')")
    @Operation(summary = "Получить объект по ID", description = "Возвращает объект по его уникальному идентификатору")
    public ResponseEntity<LocationDto> getLocationById(
            @Parameter(description = "ID объекта") @PathVariable UUID id) {
        log.info("Запрос на получение объекта с ID: {}", id);
        LocationDto location = locationService.getLocationById(id);
        return ResponseEntity.ok(location);
    }
    
    /**
     * Получает объект по названию
     * 
     * @param name название объекта
     * @return объект
     */
    @GetMapping("/name/{name}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE_MANAGER') or hasRole('OBJECT_USER')")
    @Operation(summary = "Получить объект по названию", description = "Возвращает объект по его названию")
    public ResponseEntity<LocationDto> getLocationByName(
            @Parameter(description = "Название объекта") @PathVariable String name) {
        log.info("Запрос на получение объекта по названию: {}", name);
        LocationDto location = locationService.getLocationByName(name);
        return ResponseEntity.ok(location);
    }
    
    /**
     * Получает все объекты с пагинацией
     * 
     * @param pageable параметры пагинации
     * @return страница объектов
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE_MANAGER') or hasRole('OBJECT_USER')")
    @Operation(summary = "Получить все объекты", description = "Возвращает список всех объектов с пагинацией")
    public ResponseEntity<Page<LocationDto>> getAllLocations(Pageable pageable) {
        log.info("Запрос на получение всех объектов с пагинацией");
        Page<LocationDto> locations = locationService.getAllLocations(pageable);
        return ResponseEntity.ok(locations);
    }
    
    /**
     * Получает активные объекты
     * 
     * @return список активных объектов
     */
    @GetMapping("/active")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE_MANAGER') or hasRole('OBJECT_USER')")
    @Operation(summary = "Получить активные объекты", description = "Возвращает список всех активных объектов")
    public ResponseEntity<List<LocationDto>> getActiveLocations() {
        log.info("Запрос на получение активных объектов");
        List<LocationDto> locations = locationService.getActiveLocations();
        return ResponseEntity.ok(locations);
    }
    
    /**
     * Ищет объекты по адресу
     * 
     * @param address адрес для поиска
     * @return список найденных объектов
     */
    @GetMapping("/search/address")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE_MANAGER') or hasRole('OBJECT_USER')")
    @Operation(summary = "Поиск объектов по адресу", description = "Ищет объекты по частичному совпадению адреса")
    public ResponseEntity<List<LocationDto>> searchLocationsByAddress(
            @Parameter(description = "Адрес для поиска") @RequestParam String address) {
        log.info("Запрос на поиск объектов по адресу: {}", address);
        List<LocationDto> locations = locationService.searchLocationsByAddress(address);
        return ResponseEntity.ok(locations);
    }
    
    /**
     * Ищет объекты по контактному лицу
     * 
     * @param contactPerson контактное лицо для поиска
     * @return список найденных объектов
     */
    @GetMapping("/search/contact")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE_MANAGER') or hasRole('OBJECT_USER')")
    @Operation(summary = "Поиск объектов по контактному лицу", description = "Ищет объекты по частичному совпадению контактного лица")
    public ResponseEntity<List<LocationDto>> searchLocationsByContactPerson(
            @Parameter(description = "Контактное лицо для поиска") @RequestParam String contactPerson) {
        log.info("Запрос на поиск объектов по контактному лицу: {}", contactPerson);
        List<LocationDto> locations = locationService.searchLocationsByContactPerson(contactPerson);
        return ResponseEntity.ok(locations);
    }
    
    /**
     * Обновляет объект
     * 
     * @param id идентификатор объекта
     * @param request данные для обновления
     * @return обновленный объект
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE_MANAGER')")
    @Operation(summary = "Обновить объект", description = "Обновляет существующий объект")
    public ResponseEntity<LocationDto> updateLocation(
            @Parameter(description = "ID объекта") @PathVariable UUID id,
            @Valid @RequestBody CreateLocationRequest request) {
        log.info("Запрос на обновление объекта с ID: {}", id);
        LocationDto updated = locationService.updateLocation(id, request);
        return ResponseEntity.ok(updated);
    }
    
    /**
     * Удаляет объект
     * 
     * @param id идентификатор объекта
     * @return ответ без содержимого
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Удалить объект", description = "Удаляет объект из системы")
    public ResponseEntity<Void> deleteLocation(
            @Parameter(description = "ID объекта") @PathVariable UUID id) {
        log.info("Запрос на удаление объекта с ID: {}", id);
        locationService.deleteLocation(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Изменяет статус объекта
     * 
     * @param id идентификатор объекта
     * @param active новый статус активности
     * @return ответ без содержимого
     */
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE_MANAGER')")
    @Operation(summary = "Изменить статус объекта", description = "Активирует или деактивирует объект")
    public ResponseEntity<Void> changeLocationStatus(
            @Parameter(description = "ID объекта") @PathVariable UUID id,
            @Parameter(description = "Новый статус активности") @RequestParam boolean active) {
        log.info("Запрос на изменение статуса объекта с ID: {} на активный: {}", id, active);
        locationService.changeLocationStatus(id, active);
        return ResponseEntity.ok().build();
    }
} 