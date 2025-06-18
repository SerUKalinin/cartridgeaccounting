package com.example.cartridgeaccounting.service.impl;

import com.example.cartridgeaccounting.dto.CreateOperationRequest;
import com.example.cartridgeaccounting.dto.OperationDto;
import com.example.cartridgeaccounting.entity.Cartridge;
import com.example.cartridgeaccounting.entity.Location;
import com.example.cartridgeaccounting.entity.Operation;
import com.example.cartridgeaccounting.entity.User;
import com.example.cartridgeaccounting.entity.enums.CartridgeStatus;
import com.example.cartridgeaccounting.entity.enums.OperationType;
import com.example.cartridgeaccounting.exception.CartridgeNotFoundException;
import com.example.cartridgeaccounting.exception.InvalidOperationException;
import com.example.cartridgeaccounting.exception.LocationNotFoundException;
import com.example.cartridgeaccounting.exception.UserNotFoundException;
import com.example.cartridgeaccounting.repository.CartridgeRepository;
import com.example.cartridgeaccounting.repository.LocationRepository;
import com.example.cartridgeaccounting.repository.OperationRepository;
import com.example.cartridgeaccounting.repository.UserRepository;
import com.example.cartridgeaccounting.service.OperationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OperationServiceImpl implements OperationService {
    
    private final OperationRepository operationRepository;
    private final CartridgeRepository cartridgeRepository;
    private final LocationRepository locationRepository;
    private final UserRepository userRepository;
    
    @Override
    public OperationDto createOperation(CreateOperationRequest request, String username) {
        log.info("Creating operation of type: {} for cartridge: {} by user: {}", 
                request.getType(), request.getCartridgeId(), username);
        
        Cartridge cartridge = cartridgeRepository.findById(request.getCartridgeId())
                .orElseThrow(() -> new CartridgeNotFoundException(request.getCartridgeId()));
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        
        Location location = null;
        if (request.getLocationId() != null) {
            location = locationRepository.findById(request.getLocationId())
                    .orElseThrow(() -> new LocationNotFoundException(request.getLocationId()));
        }
        
        validateOperation(cartridge, request.getType(), request.getCount());
        
        Operation operation = new Operation();
        operation.setType(request.getType());
        operation.setCount(request.getCount());
        operation.setCartridge(cartridge);
        operation.setLocation(location);
        operation.setPerformedBy(user);
        operation.setNotes(request.getNotes());
        
        updateCartridgeStatus(cartridge, request.getType(), location);
        
        Operation savedOperation = operationRepository.save(operation);
        log.info("Operation created with ID: {}", savedOperation.getId());
        
        return convertToDto(savedOperation);
    }
    
    @Override
    @Transactional(readOnly = true)
    public OperationDto getOperationById(UUID id) {
        log.info("Getting operation by ID: {}", id);
        Operation operation = operationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Operation not found with ID: " + id));
        return convertToDto(operation);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<OperationDto> getAllOperations(Pageable pageable) {
        log.info("Getting all operations with pagination");
        return operationRepository.findAll(pageable).map(this::convertToDto);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<OperationDto> getOperationsByCartridge(UUID cartridgeId, Pageable pageable) {
        log.info("Getting operations by cartridge ID: {}", cartridgeId);
        Cartridge cartridge = cartridgeRepository.findById(cartridgeId)
                .orElseThrow(() -> new CartridgeNotFoundException(cartridgeId));
        
        return operationRepository.findByCartridgeOrderByDateDesc(cartridge, pageable)
                .map(this::convertToDto);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<OperationDto> getOperationsByLocation(UUID locationId, Pageable pageable) {
        log.info("Getting operations by location ID: {}", locationId);
        Location location = locationRepository.findById(locationId)
                .orElseThrow(() -> new LocationNotFoundException(locationId));
        
        return operationRepository.findByLocationOrderByDateDesc(location, pageable)
                .map(this::convertToDto);
    }
    
    @Override
    @Transactional(readOnly = true)
    public Page<OperationDto> getOperationsByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) {
        log.info("Getting operations by date range: {} to {}", startDate, endDate);
        return operationRepository.findByDateRangeOrderByDateDesc(startDate, endDate, pageable)
                .map(this::convertToDto);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<OperationDto> getOperationsByType(OperationType type) {
        log.info("Getting operations by type: {}", type);
        return operationRepository.findByType(type).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public long getOperationCountByTypeAndDateRange(OperationType type, LocalDateTime startDate, LocalDateTime endDate) {
        return operationRepository.countByTypeAndDateRange(type, startDate, endDate);
    }
    
    private void validateOperation(Cartridge cartridge, OperationType type, Integer count) {
        switch (type) {
            case RECEIPT:
                if (cartridge.getStatus() != CartridgeStatus.IN_STOCK) {
                    throw new InvalidOperationException("RECEIPT", "Картридж уже не на складе");
                }
                break;
            case ISSUE:
                if (cartridge.getStatus() != CartridgeStatus.IN_STOCK) {
                    throw new InvalidOperationException("ISSUE", "Картридж не на складе");
                }
                break;
            case RETURN:
                if (cartridge.getStatus() != CartridgeStatus.IN_USE) {
                    throw new InvalidOperationException("RETURN", "Картридж не в использовании");
                }
                break;
            case REFILL:
                if (cartridge.getStatus() != CartridgeStatus.IN_USE) {
                    throw new InvalidOperationException("REFILL", "Картридж не в использовании");
                }
                break;
            case DISPOSAL:
                if (cartridge.getStatus() == CartridgeStatus.DISPOSED) {
                    throw new InvalidOperationException("DISPOSAL", "Картридж уже списан");
                }
                break;
        }
    }
    
    private void updateCartridgeStatus(Cartridge cartridge, OperationType type, Location location) {
        switch (type) {
            case RECEIPT:
                cartridge.setStatus(CartridgeStatus.IN_STOCK);
                cartridge.setCurrentLocation(location);
                break;
            case ISSUE:
                cartridge.setStatus(CartridgeStatus.IN_USE);
                cartridge.setCurrentLocation(location);
                break;
            case RETURN:
                cartridge.setStatus(CartridgeStatus.IN_STOCK);
                cartridge.setCurrentLocation(location);
                break;
            case REFILL:
                cartridge.setStatus(CartridgeStatus.REFILLING);
                cartridge.setCurrentLocation(null);
                break;
            case DISPOSAL:
                cartridge.setStatus(CartridgeStatus.DISPOSED);
                cartridge.setCurrentLocation(null);
                break;
        }
        cartridgeRepository.save(cartridge);
    }
    
    private OperationDto convertToDto(Operation operation) {
        OperationDto dto = new OperationDto();
        dto.setId(operation.getId());
        dto.setType(operation.getType());
        dto.setCount(operation.getCount());
        dto.setOperationDate(operation.getOperationDate());
        dto.setNotes(operation.getNotes());
        
        dto.setCartridgeId(operation.getCartridge().getId());
        dto.setCartridgeModel(operation.getCartridge().getModel());
        dto.setCartridgeSerialNumber(operation.getCartridge().getSerialNumber());
        
        if (operation.getLocation() != null) {
            dto.setLocationId(operation.getLocation().getId());
            dto.setLocationName(operation.getLocation().getName());
        }
        
        dto.setPerformedById(operation.getPerformedBy().getId());
        dto.setPerformedByUsername(operation.getPerformedBy().getUsername());
        
        return dto;
    }
} 