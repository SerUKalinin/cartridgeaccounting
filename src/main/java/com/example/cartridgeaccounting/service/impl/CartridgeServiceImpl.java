package com.example.cartridgeaccounting.service.impl;

import com.example.cartridgeaccounting.dto.CartridgeDto;
import com.example.cartridgeaccounting.dto.CreateCartridgeRequest;
import com.example.cartridgeaccounting.dto.CreateOperationRequest;
import com.example.cartridgeaccounting.entity.Cartridge;
import com.example.cartridgeaccounting.entity.Location;
import com.example.cartridgeaccounting.entity.enums.CartridgeStatus;
import com.example.cartridgeaccounting.entity.enums.OperationType;
import com.example.cartridgeaccounting.exception.CartridgeNotFoundException;
import com.example.cartridgeaccounting.exception.DuplicateSerialNumberException;
import com.example.cartridgeaccounting.exception.LocationNotFoundException;
import com.example.cartridgeaccounting.repository.CartridgeRepository;
import com.example.cartridgeaccounting.repository.LocationRepository;
import com.example.cartridgeaccounting.service.CartridgeService;
import com.example.cartridgeaccounting.service.OperationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для работы с картриджами.
 * Предоставляет методы для создания, обновления, удаления и поиска картриджей.
 * 
 * @author Система учёта картриджей
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CartridgeServiceImpl implements CartridgeService {
    
    private final CartridgeRepository cartridgeRepository;
    private final LocationRepository locationRepository;
    private final OperationService operationService;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public CartridgeDto createCartridge(CreateCartridgeRequest request) {
        log.info("Создание картриджа с моделью: {}", request.getModel());
        
        // Проверка на дублирование серийного номера
        if (request.getSerialNumber() != null && !request.getSerialNumber().trim().isEmpty()) {
            if (cartridgeRepository.existsBySerialNumber(request.getSerialNumber())) {
                throw new DuplicateSerialNumberException(request.getSerialNumber());
            }
        }
        
        Cartridge cartridge = new Cartridge();
        cartridge.setModel(request.getModel());
        cartridge.setSerialNumber(request.getSerialNumber());
        cartridge.setResourcePages(request.getResourcePages());
        cartridge.setDescription(request.getDescription());
        cartridge.setStatus(CartridgeStatus.IN_STOCK);
        cartridge.setBrand(request.getBrand());
        cartridge.setPartNumber(request.getPartNumber());
        cartridge.setColor(request.getColor());
        cartridge.setCompatiblePrinters(request.getCompatiblePrinters());
        
        // Устанавливаем местоположение при создании
        if (request.getCurrentLocationId() != null) {
            Location location = locationRepository.findById(request.getCurrentLocationId())
                    .orElseThrow(() -> new LocationNotFoundException(request.getCurrentLocationId()));
            cartridge.setCurrentLocation(location);
        }
        
        Cartridge savedCartridge = cartridgeRepository.save(cartridge);
        log.info("Картридж создан с ID: {}", savedCartridge.getId());
        
        // Создаем операцию поступления для нового картриджа
        createReceiptOperation(savedCartridge);
        
        return convertToDto(savedCartridge);
    }
    
    /**
     * Создает операцию поступления для нового картриджа
     */
    private void createReceiptOperation(Cartridge cartridge) {
        String username = getCurrentUsername();
        
        CreateOperationRequest operationRequest = new CreateOperationRequest();
        operationRequest.setType(OperationType.RECEIPT);
        operationRequest.setCartridgeId(cartridge.getId());
        operationRequest.setLocationId(cartridge.getCurrentLocation() != null ? cartridge.getCurrentLocation().getId() : null);
        operationRequest.setCount(1);
        operationRequest.setNotes("Первичное поступление картриджа на склад");
        
        try {
            operationService.createOperation(operationRequest, username);
            log.info("Создана операция поступления для нового картриджа {}", cartridge.getId());
        } catch (Exception e) {
            log.error("Ошибка при создании операции поступления: {}", e.getMessage());
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public CartridgeDto getCartridgeById(UUID id) {
        log.info("Получение картриджа по ID: {}", id);
        Cartridge cartridge = cartridgeRepository.findById(id)
                .orElseThrow(() -> new CartridgeNotFoundException(id));
        return convertToDto(cartridge);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public CartridgeDto getCartridgeBySerialNumber(String serialNumber) {
        log.info("Получение картриджа по серийному номеру: {}", serialNumber);
        Cartridge cartridge = cartridgeRepository.findBySerialNumber(serialNumber)
                .orElseThrow(() -> new CartridgeNotFoundException(serialNumber, "не найден"));
        return convertToDto(cartridge);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CartridgeDto> getAllCartridges(Pageable pageable) {
        log.info("Получение всех картриджей с пагинацией");
        return cartridgeRepository.findAll(pageable).map(this::convertToDto);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<CartridgeDto> searchCartridges(String model, String serialNumber, Pageable pageable) {
        log.info("Поиск картриджей с моделью: {}, серийным номером: {}", model, serialNumber);
        return cartridgeRepository.findByModelOrSerialNumberContaining(model, serialNumber, pageable)
                .map(this::convertToDto);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<CartridgeDto> getCartridgesByStatus(CartridgeStatus status) {
        log.info("Получение картриджей по статусу: {}", status);
        return cartridgeRepository.findByStatus(status).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<CartridgeDto> getCartridgesByLocation(UUID locationId) {
        log.info("Получение картриджей по объекту с ID: {}", locationId);
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new LocationNotFoundException(locationId));
        
        return cartridgeRepository.findByCurrentLocation(location).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public CartridgeDto updateCartridge(UUID id, CreateCartridgeRequest request) {
        log.info("Обновление картриджа с ID: {}", id);
        Cartridge cartridge = cartridgeRepository.findById(id)
                .orElseThrow(() -> new CartridgeNotFoundException(id));
        
        // Сохраняем старые значения для определения изменений
        CartridgeStatus oldStatus = cartridge.getStatus();
        Location oldLocation = cartridge.getCurrentLocation();
        
        // Проверка на дублирование серийного номера (если изменился)
        if (request.getSerialNumber() != null && !request.getSerialNumber().equals(cartridge.getSerialNumber())) {
            if (cartridgeRepository.existsBySerialNumber(request.getSerialNumber())) {
                throw new DuplicateSerialNumberException(request.getSerialNumber());
            }
        }
        
        cartridge.setModel(request.getModel());
        cartridge.setSerialNumber(request.getSerialNumber());
        cartridge.setResourcePages(request.getResourcePages());
        cartridge.setDescription(request.getDescription());
        cartridge.setStatus(request.getStatus());
        cartridge.setBrand(request.getBrand());
        cartridge.setPartNumber(request.getPartNumber());
        cartridge.setColor(request.getColor());
        cartridge.setCompatiblePrinters(request.getCompatiblePrinters());
        
        // Обновляем местоположение
        Location newLocation = null;
        if (request.getCurrentLocationId() != null) {
            newLocation = locationRepository.findById(request.getCurrentLocationId())
                    .orElseThrow(() -> new LocationNotFoundException(request.getCurrentLocationId()));
            cartridge.setCurrentLocation(newLocation);
        } else {
            cartridge.setCurrentLocation(null);
        }
        
        Cartridge updatedCartridge = cartridgeRepository.save(cartridge);
        log.info("Картридж обновлен с ID: {}", updatedCartridge.getId());
        
        // Создаем операции для изменений статуса и местоположения
        createOperationsForChanges(cartridge, oldStatus, oldLocation, request.getStatus(), newLocation);
        
        return convertToDto(updatedCartridge);
    }
    
    /**
     * Создает операции для изменений статуса и местоположения картриджа
     */
    private void createOperationsForChanges(Cartridge cartridge, CartridgeStatus oldStatus, 
                                          Location oldLocation, CartridgeStatus newStatus, Location newLocation) {
        String username = getCurrentUsername();
        
        // Создаем операцию для изменения статуса
        if (oldStatus != newStatus) {
            OperationType operationType = determineOperationType(oldStatus, newStatus);
            if (operationType != null) {
                CreateOperationRequest operationRequest = new CreateOperationRequest();
                operationRequest.setType(operationType);
                operationRequest.setCartridgeId(cartridge.getId());
                operationRequest.setLocationId(newLocation != null ? newLocation.getId() : null);
                operationRequest.setCount(1);
                operationRequest.setNotes("Изменение статуса с " + oldStatus.getDescription() + 
                                        " на " + newStatus.getDescription());
                
                try {
                    operationService.createOperation(operationRequest, username);
                    log.info("Создана операция типа {} для картриджа {}", operationType, cartridge.getId());
                } catch (Exception e) {
                    log.error("Ошибка при создании операции для изменения статуса: {}", e.getMessage());
                }
            }
        }
        
        // Создаем операцию для изменения местоположения (если статус не изменился)
        if (oldStatus == newStatus && oldLocation != newLocation) {
            OperationType operationType = determineLocationChangeOperationType(newStatus);
            if (operationType != null) {
                CreateOperationRequest operationRequest = new CreateOperationRequest();
                operationRequest.setType(operationType);
                operationRequest.setCartridgeId(cartridge.getId());
                operationRequest.setLocationId(newLocation != null ? newLocation.getId() : null);
                operationRequest.setCount(1);
                operationRequest.setNotes("Перемещение картриджа" + 
                                        (oldLocation != null ? " из " + oldLocation.getName() : "") +
                                        (newLocation != null ? " в " + newLocation.getName() : ""));
                
                try {
                    operationService.createOperation(operationRequest, username);
                    log.info("Создана операция типа {} для перемещения картриджа {}", operationType, cartridge.getId());
                } catch (Exception e) {
                    log.error("Ошибка при создании операции для перемещения: {}", e.getMessage());
                }
            }
        }
    }
    
    /**
     * Определяет тип операции на основе изменения статуса
     */
    private OperationType determineOperationType(CartridgeStatus oldStatus, CartridgeStatus newStatus) {
        if (oldStatus == CartridgeStatus.IN_STOCK && newStatus == CartridgeStatus.IN_USE) {
            return OperationType.ISSUE;
        } else if (oldStatus == CartridgeStatus.IN_USE && newStatus == CartridgeStatus.IN_STOCK) {
            return OperationType.RETURN;
        } else if (newStatus == CartridgeStatus.REFILLING) {
            return OperationType.REFILL;
        } else if (newStatus == CartridgeStatus.DISPOSED) {
            return OperationType.DISPOSAL;
        } else if (oldStatus == CartridgeStatus.REFILLING && newStatus == CartridgeStatus.IN_STOCK) {
            return OperationType.RECEIPT;
        }
        return null;
    }
    
    /**
     * Определяет тип операции для изменения местоположения
     */
    private OperationType determineLocationChangeOperationType(CartridgeStatus status) {
        switch (status) {
            case IN_STOCK:
                return OperationType.RECEIPT;
            case IN_USE:
                return OperationType.ISSUE;
            default:
                return null;
        }
    }
    
    /**
     * Получает имя текущего пользователя
     */
    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return "system"; // fallback для системных операций
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteCartridge(UUID id) {
        log.info("Удаление картриджа с ID: {}", id);
        Cartridge cartridge = cartridgeRepository.findById(id)
                .orElseThrow(() -> new CartridgeNotFoundException(id));
        
        // Создаем операцию списания перед удалением
        createDisposalOperation(cartridge);
        
        cartridgeRepository.deleteById(id);
        log.info("Картридж удален с ID: {}", id);
    }
    
    /**
     * Создает операцию списания для удаляемого картриджа
     */
    private void createDisposalOperation(Cartridge cartridge) {
        String username = getCurrentUsername();
        
        CreateOperationRequest operationRequest = new CreateOperationRequest();
        operationRequest.setType(OperationType.DISPOSAL);
        operationRequest.setCartridgeId(cartridge.getId());
        operationRequest.setLocationId(null); // При списании местоположение не указывается
        operationRequest.setCount(1);
        operationRequest.setNotes("Списание картриджа из системы");
        
        try {
            operationService.createOperation(operationRequest, username);
            log.info("Создана операция списания для картриджа {}", cartridge.getId());
        } catch (Exception e) {
            log.error("Ошибка при создании операции списания: {}", e.getMessage());
        }
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public long getCartridgeCountByStatus(CartridgeStatus status) {
        return cartridgeRepository.countByStatus(status);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public long getCartridgeCountByLocationAndStatus(UUID locationId, CartridgeStatus status) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new LocationNotFoundException(locationId));
        return cartridgeRepository.countByCurrentLocationAndStatus(location, status);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public long getTotalCartridgeCount() {
        return cartridgeRepository.count();
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public long getCartridgeCountByLocation(UUID locationId) {
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new LocationNotFoundException(locationId));
        return cartridgeRepository.countByCurrentLocation(location);
    }
    
    /**
     * Конвертирует сущность картриджа в DTO
     */
    private CartridgeDto convertToDto(Cartridge cartridge) {
        CartridgeDto dto = new CartridgeDto();
        dto.setId(cartridge.getId());
        dto.setModel(cartridge.getModel());
        dto.setSerialNumber(cartridge.getSerialNumber());
        dto.setResourcePages(cartridge.getResourcePages());
        dto.setDescription(cartridge.getDescription());
        dto.setStatus(cartridge.getStatus());
        dto.setCurrentLocationId(cartridge.getCurrentLocation() != null ? cartridge.getCurrentLocation().getId() : null);
        dto.setCurrentLocationName(cartridge.getCurrentLocation() != null ? cartridge.getCurrentLocation().getName() : null);
        dto.setCreatedAt(cartridge.getCreatedAt());
        dto.setUpdatedAt(cartridge.getUpdatedAt());
        dto.setBrand(cartridge.getBrand());
        dto.setPartNumber(cartridge.getPartNumber());
        dto.setColor(cartridge.getColor());
        dto.setCompatiblePrinters(cartridge.getCompatiblePrinters());
        return dto;
    }
} 