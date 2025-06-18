package com.example.cartridgeaccounting.controller;

import com.example.cartridgeaccounting.dto.CreateOperationRequest;
import com.example.cartridgeaccounting.dto.OperationDto;
import com.example.cartridgeaccounting.entity.enums.OperationType;
import com.example.cartridgeaccounting.service.OperationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/operations")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Операции", description = "API для работы с операциями картриджей")
public class OperationController {
    
    private final OperationService operationService;
    
    @PostMapping
    @Operation(summary = "Создать операцию", description = "Создает новую операцию с картриджем")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE_MANAGER')")
    public ResponseEntity<OperationDto> createOperation(
            @Valid @RequestBody CreateOperationRequest request,
            Authentication authentication) {
        log.info("Creating operation of type: {} for cartridge: {}", request.getType(), request.getCartridgeId());
        String username = authentication.getName();
        OperationDto createdOperation = operationService.createOperation(request, username);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOperation);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Получить операцию по ID", description = "Возвращает операцию по её уникальному идентификатору")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE_MANAGER') or hasRole('OBJECT_USER')")
    public ResponseEntity<OperationDto> getOperationById(@PathVariable UUID id) {
        log.info("Getting operation by ID: {}", id);
        OperationDto operation = operationService.getOperationById(id);
        return ResponseEntity.ok(operation);
    }
    
    @GetMapping
    @Operation(summary = "Получить все операции", description = "Возвращает список всех операций с пагинацией")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE_MANAGER') or hasRole('OBJECT_USER')")
    public ResponseEntity<Page<OperationDto>> getAllOperations(
            @Parameter(description = "Номер страницы") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы") @RequestParam(defaultValue = "20") int size) {
        log.info("Getting all operations with page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        Page<OperationDto> operations = operationService.getAllOperations(pageable);
        return ResponseEntity.ok(operations);
    }
    
    @GetMapping("/cartridge/{cartridgeId}")
    @Operation(summary = "Получить операции по картриджу", description = "Возвращает историю операций для конкретного картриджа")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE_MANAGER') or hasRole('OBJECT_USER')")
    public ResponseEntity<Page<OperationDto>> getOperationsByCartridge(
            @PathVariable UUID cartridgeId,
            @Parameter(description = "Номер страницы") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы") @RequestParam(defaultValue = "20") int size) {
        log.info("Getting operations by cartridge ID: {}", cartridgeId);
        Pageable pageable = PageRequest.of(page, size);
        Page<OperationDto> operations = operationService.getOperationsByCartridge(cartridgeId, pageable);
        return ResponseEntity.ok(operations);
    }
    
    @GetMapping("/location/{locationId}")
    @Operation(summary = "Получить операции по объекту", description = "Возвращает операции для конкретного объекта")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE_MANAGER') or hasRole('OBJECT_USER')")
    public ResponseEntity<Page<OperationDto>> getOperationsByLocation(
            @PathVariable UUID locationId,
            @Parameter(description = "Номер страницы") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы") @RequestParam(defaultValue = "20") int size) {
        log.info("Getting operations by location ID: {}", locationId);
        Pageable pageable = PageRequest.of(page, size);
        Page<OperationDto> operations = operationService.getOperationsByLocation(locationId, pageable);
        return ResponseEntity.ok(operations);
    }
    
    @GetMapping("/date-range")
    @Operation(summary = "Получить операции по диапазону дат", description = "Возвращает операции в указанном диапазоне дат")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE_MANAGER') or hasRole('OBJECT_USER')")
    public ResponseEntity<Page<OperationDto>> getOperationsByDateRange(
            @Parameter(description = "Начальная дата") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "Конечная дата") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @Parameter(description = "Номер страницы") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Размер страницы") @RequestParam(defaultValue = "20") int size) {
        log.info("Getting operations by date range: {} to {}", startDate, endDate);
        Pageable pageable = PageRequest.of(page, size);
        Page<OperationDto> operations = operationService.getOperationsByDateRange(startDate, endDate, pageable);
        return ResponseEntity.ok(operations);
    }
    
    @GetMapping("/type/{type}")
    @Operation(summary = "Получить операции по типу", description = "Возвращает операции определенного типа")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE_MANAGER') or hasRole('OBJECT_USER')")
    public ResponseEntity<List<OperationDto>> getOperationsByType(@PathVariable OperationType type) {
        log.info("Getting operations by type: {}", type);
        List<OperationDto> operations = operationService.getOperationsByType(type);
        return ResponseEntity.ok(operations);
    }
    
    @GetMapping("/count/type/{type}")
    @Operation(summary = "Получить количество операций по типу и датам", description = "Возвращает количество операций определенного типа в диапазоне дат")
    @PreAuthorize("hasRole('ADMIN') or hasRole('WAREHOUSE_MANAGER') or hasRole('OBJECT_USER')")
    public ResponseEntity<Long> getOperationCountByTypeAndDateRange(
            @PathVariable OperationType type,
            @Parameter(description = "Начальная дата") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @Parameter(description = "Конечная дата") 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        log.info("Getting operation count by type: {} and date range: {} to {}", type, startDate, endDate);
        long count = operationService.getOperationCountByTypeAndDateRange(type, startDate, endDate);
        return ResponseEntity.ok(count);
    }
} 