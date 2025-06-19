package com.example.cartridgeaccounting.service;

import com.example.cartridgeaccounting.dto.CartridgeDto;
import com.example.cartridgeaccounting.dto.OperationDto;
import com.example.cartridgeaccounting.dto.UserDto;
import com.example.cartridgeaccounting.dto.LocationDto;
import com.example.cartridgeaccounting.entity.enums.CartridgeStatus;
import com.example.cartridgeaccounting.entity.enums.OperationType;
import com.example.cartridgeaccounting.entity.enums.UserRole;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExcelExportService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    public byte[] exportCartridges(List<CartridgeDto> cartridges) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Картриджи");

            // Создаем стили
            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dateStyle = createDateStyle(workbook);

            // Создаем заголовки
            Row headerRow = sheet.createRow(0);
            String[] headers = {
                "ID", "Модель", "Бренд", "Артикул", "Цвет", "Совместимость", 
                "Серийный номер", "Статус", "Ресурс (стр.)", "Местоположение", "Дата создания"
            };

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, 4000);
            }

            // Заполняем данные
            int rowNum = 1;
            for (CartridgeDto cartridge : cartridges) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(cartridge.getId().toString());
                row.createCell(1).setCellValue(cartridge.getModel());
                row.createCell(2).setCellValue(cartridge.getBrand() != null ? cartridge.getBrand() : "");
                row.createCell(3).setCellValue(cartridge.getPartNumber() != null ? cartridge.getPartNumber() : "");
                row.createCell(4).setCellValue(cartridge.getColor() != null ? cartridge.getColor() : "");
                row.createCell(5).setCellValue(cartridge.getCompatiblePrinters() != null ? cartridge.getCompatiblePrinters() : "");
                row.createCell(6).setCellValue(cartridge.getSerialNumber() != null ? cartridge.getSerialNumber() : "");
                row.createCell(7).setCellValue(getStatusLabel(cartridge.getStatus().name()));
                row.createCell(8).setCellValue(cartridge.getResourcePages() != null ? cartridge.getResourcePages() : 0);
                row.createCell(9).setCellValue(cartridge.getCurrentLocationName() != null ? cartridge.getCurrentLocationName() : "");
                
                Cell dateCell = row.createCell(10);
                dateCell.setCellValue(cartridge.getCreatedAt().format(DATE_FORMATTER));
                dateCell.setCellStyle(dateStyle);
            }

            return writeToByteArray(workbook);
        }
    }

    public byte[] exportOperations(List<OperationDto> operations) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Операции");

            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dateStyle = createDateStyle(workbook);

            Row headerRow = sheet.createRow(0);
            String[] headers = {
                "ID", "Тип операции", "Картридж", "Пользователь", "Местоположение", 
                "Дата операции", "Комментарий"
            };

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, 4000);
            }

            int rowNum = 1;
            for (OperationDto operation : operations) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(operation.getId().toString());
                row.createCell(1).setCellValue(getOperationTypeLabel(operation.getType().name()));
                row.createCell(2).setCellValue(operation.getCartridgeModel() != null ? operation.getCartridgeModel() : "");
                row.createCell(3).setCellValue(operation.getPerformedByUsername() != null ? operation.getPerformedByUsername() : "");
                row.createCell(4).setCellValue(operation.getLocationName() != null ? operation.getLocationName() : "");
                
                Cell dateCell = row.createCell(5);
                dateCell.setCellValue(operation.getOperationDate().format(DATE_FORMATTER));
                dateCell.setCellStyle(dateStyle);
                
                row.createCell(6).setCellValue(operation.getNotes() != null ? operation.getNotes() : "");
            }

            return writeToByteArray(workbook);
        }
    }

    public byte[] exportUsers(List<UserDto> users) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Пользователи");

            CellStyle headerStyle = createHeaderStyle(workbook);

            Row headerRow = sheet.createRow(0);
            String[] headers = {
                "ID", "Имя пользователя", "Полное имя", "Роль", "Активен"
            };

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, 4000);
            }

            int rowNum = 1;
            for (UserDto user : users) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(user.getId().toString());
                row.createCell(1).setCellValue(user.getUsername());
                row.createCell(2).setCellValue(user.getFullName() != null ? user.getFullName() : "");
                row.createCell(3).setCellValue(getRoleLabel(user.getRole().name()));
                row.createCell(4).setCellValue(user.isEnabled() ? "Да" : "Нет");
            }

            return writeToByteArray(workbook);
        }
    }

    public byte[] exportLocations(List<LocationDto> locations) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Объекты");

            CellStyle headerStyle = createHeaderStyle(workbook);

            Row headerRow = sheet.createRow(0);
            String[] headers = {
                "ID", "Название", "Адрес", "Кабинет", "Количество картриджей", "Активен"
            };

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
                sheet.setColumnWidth(i, 4000);
            }

            int rowNum = 1;
            for (LocationDto location : locations) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(location.getId().toString());
                row.createCell(1).setCellValue(location.getName());
                row.createCell(2).setCellValue(location.getAddress() != null ? location.getAddress() : "");
                row.createCell(3).setCellValue(location.getCabinet() != null ? location.getCabinet() : "");
                row.createCell(4).setCellValue(location.getCartridgeCount());
                row.createCell(5).setCellValue(location.isActive() ? "Да" : "Нет");
            }

            return writeToByteArray(workbook);
        }
    }

    private CellStyle createHeaderStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setBold(true);
        style.setFont(font);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        return style;
    }

    private CellStyle createDateStyle(Workbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setDataFormat(workbook.createDataFormat().getFormat("dd.mm.yyyy hh:mm"));
        return style;
    }

    private byte[] writeToByteArray(Workbook workbook) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            workbook.write(outputStream);
            return outputStream.toByteArray();
        }
    }

    private String getStatusLabel(String status) {
        switch (status) {
            case "IN_STOCK": return "На складе";
            case "IN_USE": return "В использовании";
            case "REFILLING": return "На заправке";
            case "DISPOSED": return "Списан";
            default: return status;
        }
    }

    private String getOperationTypeLabel(String type) {
        switch (type) {
            case "RECEIPT": return "Поступление";
            case "ISSUE": return "Выдача";
            case "RETURN": return "Возврат";
            case "REFILL": return "Заправка";
            case "DISPOSAL": return "Списание";
            case "TRANSFER": return "Перемещение";
            default: return type;
        }
    }

    private String getRoleLabel(String role) {
        switch (role) {
            case "ADMIN": return "Администратор";
            case "USER": return "Пользователь";
            case "MANAGER": return "Менеджер";
            default: return role;
        }
    }

    // Методы для экспорта на отдельные листы
    public void exportCartridgesToSheet(Sheet sheet, List<CartridgeDto> cartridges) {
        CellStyle headerStyle = createHeaderStyle(sheet.getWorkbook());
        CellStyle dateStyle = createDateStyle(sheet.getWorkbook());

        // Создаем заголовки
        Row headerRow = sheet.createRow(0);
        String[] headers = {
            "ID", "Модель", "Бренд", "Артикул", "Цвет", "Совместимость", 
            "Серийный номер", "Статус", "Ресурс (стр.)", "Местоположение", "Дата создания"
        };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
            sheet.setColumnWidth(i, 4000);
        }

        // Заполняем данные
        int rowNum = 1;
        for (CartridgeDto cartridge : cartridges) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(cartridge.getId().toString());
            row.createCell(1).setCellValue(cartridge.getModel());
            row.createCell(2).setCellValue(cartridge.getBrand() != null ? cartridge.getBrand() : "");
            row.createCell(3).setCellValue(cartridge.getPartNumber() != null ? cartridge.getPartNumber() : "");
            row.createCell(4).setCellValue(cartridge.getColor() != null ? cartridge.getColor() : "");
            row.createCell(5).setCellValue(cartridge.getCompatiblePrinters() != null ? cartridge.getCompatiblePrinters() : "");
            row.createCell(6).setCellValue(cartridge.getSerialNumber() != null ? cartridge.getSerialNumber() : "");
            row.createCell(7).setCellValue(getStatusLabel(cartridge.getStatus().name()));
            row.createCell(8).setCellValue(cartridge.getResourcePages() != null ? cartridge.getResourcePages() : 0);
            row.createCell(9).setCellValue(cartridge.getCurrentLocationName() != null ? cartridge.getCurrentLocationName() : "");
            
            Cell dateCell = row.createCell(10);
            dateCell.setCellValue(cartridge.getCreatedAt().format(DATE_FORMATTER));
            dateCell.setCellStyle(dateStyle);
        }
    }

    public void exportOperationsToSheet(Sheet sheet, List<OperationDto> operations) {
        CellStyle headerStyle = createHeaderStyle(sheet.getWorkbook());
        CellStyle dateStyle = createDateStyle(sheet.getWorkbook());

        Row headerRow = sheet.createRow(0);
        String[] headers = {
            "ID", "Тип операции", "Картридж", "Пользователь", "Местоположение", 
            "Дата операции", "Комментарий"
        };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
            sheet.setColumnWidth(i, 4000);
        }

        int rowNum = 1;
        for (OperationDto operation : operations) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(operation.getId().toString());
            row.createCell(1).setCellValue(getOperationTypeLabel(operation.getType().name()));
            row.createCell(2).setCellValue(operation.getCartridgeModel() != null ? operation.getCartridgeModel() : "");
            row.createCell(3).setCellValue(operation.getPerformedByUsername() != null ? operation.getPerformedByUsername() : "");
            row.createCell(4).setCellValue(operation.getLocationName() != null ? operation.getLocationName() : "");
            
            Cell dateCell = row.createCell(5);
            dateCell.setCellValue(operation.getOperationDate().format(DATE_FORMATTER));
            dateCell.setCellStyle(dateStyle);
            
            row.createCell(6).setCellValue(operation.getNotes() != null ? operation.getNotes() : "");
        }
    }

    public void exportUsersToSheet(Sheet sheet, List<UserDto> users) {
        CellStyle headerStyle = createHeaderStyle(sheet.getWorkbook());

        Row headerRow = sheet.createRow(0);
        String[] headers = {
            "ID", "Имя пользователя", "Полное имя", "Роль", "Активен"
        };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
            sheet.setColumnWidth(i, 4000);
        }

        int rowNum = 1;
        for (UserDto user : users) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(user.getId().toString());
            row.createCell(1).setCellValue(user.getUsername());
            row.createCell(2).setCellValue(user.getFullName() != null ? user.getFullName() : "");
            row.createCell(3).setCellValue(getRoleLabel(user.getRole().name()));
            row.createCell(4).setCellValue(user.isEnabled() ? "Да" : "Нет");
        }
    }

    public void exportLocationsToSheet(Sheet sheet, List<LocationDto> locations) {
        CellStyle headerStyle = createHeaderStyle(sheet.getWorkbook());

        Row headerRow = sheet.createRow(0);
        String[] headers = {
            "ID", "Название", "Адрес", "Кабинет", "Количество картриджей", "Активен"
        };

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
            sheet.setColumnWidth(i, 4000);
        }

        int rowNum = 1;
        for (LocationDto location : locations) {
            Row row = sheet.createRow(rowNum++);
            row.createCell(0).setCellValue(location.getId().toString());
            row.createCell(1).setCellValue(location.getName());
            row.createCell(2).setCellValue(location.getAddress() != null ? location.getAddress() : "");
            row.createCell(3).setCellValue(location.getCabinet() != null ? location.getCabinet() : "");
            row.createCell(4).setCellValue(location.getCartridgeCount());
            row.createCell(5).setCellValue(location.isActive() ? "Да" : "Нет");
        }
    }
} 