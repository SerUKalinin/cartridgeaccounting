package com.example.cartridgeaccounting.service.impl;

import com.example.cartridgeaccounting.dto.CartridgeDto;
import com.example.cartridgeaccounting.dto.CreateCartridgeRequest;
import com.example.cartridgeaccounting.entity.Cartridge;
import com.example.cartridgeaccounting.entity.Location;
import com.example.cartridgeaccounting.entity.enums.CartridgeStatus;
import com.example.cartridgeaccounting.exception.CartridgeNotFoundException;
import com.example.cartridgeaccounting.exception.DuplicateSerialNumberException;
import com.example.cartridgeaccounting.exception.LocationNotFoundException;
import com.example.cartridgeaccounting.repository.CartridgeRepository;
import com.example.cartridgeaccounting.repository.LocationRepository;
import com.example.cartridgeaccounting.service.CartridgeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
        
        Cartridge savedCartridge = cartridgeRepository.save(cartridge);
        log.info("Картридж создан с ID: {}", savedCartridge.getId());
        
        return convertToDto(savedCartridge);
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
        
        Cartridge updatedCartridge = cartridgeRepository.save(cartridge);
        log.info("Картридж обновлен с ID: {}", updatedCartridge.getId());
        
        return convertToDto(updatedCartridge);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteCartridge(UUID id) {
        log.info("Удаление картриджа с ID: {}", id);
        if (!cartridgeRepository.existsById(id)) {
            throw new CartridgeNotFoundException(id);
        }
        cartridgeRepository.deleteById(id);
        log.info("Картридж удален с ID: {}", id);
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
        return cartridgeRepository.countByLocationAndStatus(location, status);
    }
    
    /**
     * Преобразует сущность картриджа в DTO
     * 
     * @param cartridge сущность картриджа
     * @return DTO картриджа
     */
    private CartridgeDto convertToDto(Cartridge cartridge) {
        CartridgeDto dto = new CartridgeDto();
        dto.setId(cartridge.getId());
        dto.setModel(cartridge.getModel());
        dto.setSerialNumber(cartridge.getSerialNumber());
        dto.setResourcePages(cartridge.getResourcePages());
        dto.setDescription(cartridge.getDescription());
        dto.setStatus(cartridge.getStatus());
        dto.setCreatedAt(cartridge.getCreatedAt());
        dto.setUpdatedAt(cartridge.getUpdatedAt());
        
        if (cartridge.getCurrentLocation() != null) {
            dto.setCurrentLocationId(cartridge.getCurrentLocation().getId());
            dto.setCurrentLocationName(cartridge.getCurrentLocation().getName());
        }
        
        return dto;
    }
} 