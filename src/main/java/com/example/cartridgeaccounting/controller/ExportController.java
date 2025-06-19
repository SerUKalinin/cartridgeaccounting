package com.example.cartridgeaccounting.controller;

import com.example.cartridgeaccounting.service.CartridgeService;
import com.example.cartridgeaccounting.service.OperationService;
import com.example.cartridgeaccounting.service.UserService;
import com.example.cartridgeaccounting.service.LocationService;
import com.example.cartridgeaccounting.service.ExcelExportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/export")
@RequiredArgsConstructor
@Tag(name = "Экспорт", description = "API для экспорта данных в Excel")
public class ExportController {

    private final ExcelExportService excelExportService;
    private final CartridgeService cartridgeService;
    private final OperationService operationService;
    private final UserService userService;
    private final LocationService locationService;

    @GetMapping("/cartridges")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Экспорт картриджей в Excel", description = "Экспортирует все картриджи в Excel файл")
    public ResponseEntity<byte[]> exportCartridges(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String model
    ) throws IOException {
        // Получаем все картриджи (можно добавить фильтрацию)
        Page<?> cartridgesPage = cartridgeService.getAllCartridges(PageRequest.of(0, Integer.MAX_VALUE));
        
        byte[] excelContent = excelExportService.exportCartridges(
            cartridgesPage.getContent().stream()
                .map(cartridge -> (com.example.cartridgeaccounting.dto.CartridgeDto) cartridge)
                .toList()
        );

        String filename = "cartridges_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelContent);
    }

    @GetMapping("/operations")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Экспорт операций в Excel", description = "Экспортирует все операции в Excel файл")
    public ResponseEntity<byte[]> exportOperations(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
    ) throws IOException {
        // Получаем все операции (можно добавить фильтрацию по датам)
        Page<?> operationsPage = operationService.getAllOperations(PageRequest.of(0, Integer.MAX_VALUE));
        
        byte[] excelContent = excelExportService.exportOperations(
            operationsPage.getContent().stream()
                .map(operation -> (com.example.cartridgeaccounting.dto.OperationDto) operation)
                .toList()
        );

        String filename = "operations_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelContent);
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Экспорт пользователей в Excel", description = "Экспортирует всех пользователей в Excel файл")
    public ResponseEntity<byte[]> exportUsers() throws IOException {
        Page<?> usersPage = userService.getAllUsers(PageRequest.of(0, Integer.MAX_VALUE));
        
        byte[] excelContent = excelExportService.exportUsers(
            usersPage.getContent().stream()
                .map(user -> (com.example.cartridgeaccounting.dto.UserDto) user)
                .toList()
        );

        String filename = "users_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelContent);
    }

    @GetMapping("/locations")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @Operation(summary = "Экспорт объектов в Excel", description = "Экспортирует все объекты в Excel файл")
    public ResponseEntity<byte[]> exportLocations() throws IOException {
        Page<?> locationsPage = locationService.getAllLocations(PageRequest.of(0, Integer.MAX_VALUE));
        
        byte[] excelContent = excelExportService.exportLocations(
            locationsPage.getContent().stream()
                .map(location -> (com.example.cartridgeaccounting.dto.LocationDto) location)
                .toList()
        );

        String filename = "locations_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .body(excelContent);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Экспорт всех данных в Excel", description = "Экспортирует все данные системы в один Excel файл с несколькими листами")
    public ResponseEntity<byte[]> exportAll() throws IOException {
        // Получаем все данные
        Page<?> cartridgesPage = cartridgeService.getAllCartridges(PageRequest.of(0, Integer.MAX_VALUE));
        Page<?> operationsPage = operationService.getAllOperations(PageRequest.of(0, Integer.MAX_VALUE));
        Page<?> usersPage = userService.getAllUsers(PageRequest.of(0, Integer.MAX_VALUE));
        Page<?> locationsPage = locationService.getAllLocations(PageRequest.of(0, Integer.MAX_VALUE));

        // Создаем Excel файл с несколькими листами
        try (var workbook = new org.apache.poi.xssf.usermodel.XSSFWorkbook()) {
            var cartridgesSheet = workbook.createSheet("Картриджи");
            var operationsSheet = workbook.createSheet("Операции");
            var usersSheet = workbook.createSheet("Пользователи");
            var locationsSheet = workbook.createSheet("Объекты");

            // Экспортируем каждый тип данных на отдельный лист
            excelExportService.exportCartridgesToSheet(cartridgesSheet, 
                cartridgesPage.getContent().stream()
                    .map(cartridge -> (com.example.cartridgeaccounting.dto.CartridgeDto) cartridge)
                    .toList()
            );
            
            excelExportService.exportOperationsToSheet(operationsSheet,
                operationsPage.getContent().stream()
                    .map(operation -> (com.example.cartridgeaccounting.dto.OperationDto) operation)
                    .toList()
            );
            
            excelExportService.exportUsersToSheet(usersSheet,
                usersPage.getContent().stream()
                    .map(user -> (com.example.cartridgeaccounting.dto.UserDto) user)
                    .toList()
            );
            
            excelExportService.exportLocationsToSheet(locationsSheet,
                locationsPage.getContent().stream()
                    .map(location -> (com.example.cartridgeaccounting.dto.LocationDto) location)
                    .toList()
            );

            // Записываем в байтовый массив
            try (var outputStream = new java.io.ByteArrayOutputStream()) {
                workbook.write(outputStream);
                byte[] excelContent = outputStream.toByteArray();

                String filename = "cartridge_accounting_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".xlsx";
                
                return ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                        .contentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                        .body(excelContent);
            }
        }
    }
} 