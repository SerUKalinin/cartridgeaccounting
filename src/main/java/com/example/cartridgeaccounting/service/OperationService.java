package com.example.cartridgeaccounting.service;

import com.example.cartridgeaccounting.dto.CreateOperationRequest;
import com.example.cartridgeaccounting.dto.OperationDto;
import com.example.cartridgeaccounting.entity.enums.OperationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface OperationService {
    
    OperationDto createOperation(CreateOperationRequest request, String username);
    
    OperationDto getOperationById(UUID id);
    
    Page<OperationDto> getAllOperations(Pageable pageable);
    
    Page<OperationDto> getOperationsByCartridge(UUID cartridgeId, Pageable pageable);
    
    Page<OperationDto> getOperationsByLocation(UUID locationId, Pageable pageable);
    
    Page<OperationDto> getOperationsByDateRange(LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
    
    List<OperationDto> getOperationsByType(OperationType type);
    
    long getOperationCountByTypeAndDateRange(OperationType type, LocalDateTime startDate, LocalDateTime endDate);
} 