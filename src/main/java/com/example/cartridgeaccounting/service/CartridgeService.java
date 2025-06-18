package com.example.cartridgeaccounting.service;

import com.example.cartridgeaccounting.dto.CartridgeDto;
import com.example.cartridgeaccounting.dto.CreateCartridgeRequest;
import com.example.cartridgeaccounting.entity.enums.CartridgeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface CartridgeService {
    
    CartridgeDto createCartridge(CreateCartridgeRequest request);
    
    CartridgeDto getCartridgeById(UUID id);
    
    CartridgeDto getCartridgeBySerialNumber(String serialNumber);
    
    Page<CartridgeDto> getAllCartridges(Pageable pageable);
    
    Page<CartridgeDto> searchCartridges(String model, String serialNumber, Pageable pageable);
    
    List<CartridgeDto> getCartridgesByStatus(CartridgeStatus status);
    
    List<CartridgeDto> getCartridgesByLocation(UUID locationId);
    
    CartridgeDto updateCartridge(UUID id, CreateCartridgeRequest request);
    
    void deleteCartridge(UUID id);
    
    long getCartridgeCountByStatus(CartridgeStatus status);
    
    long getCartridgeCountByLocationAndStatus(UUID locationId, CartridgeStatus status);
} 