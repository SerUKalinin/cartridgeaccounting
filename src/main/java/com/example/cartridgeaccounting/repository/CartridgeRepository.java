package com.example.cartridgeaccounting.repository;

import com.example.cartridgeaccounting.entity.Cartridge;
import com.example.cartridgeaccounting.entity.Location;
import com.example.cartridgeaccounting.entity.enums.CartridgeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CartridgeRepository extends JpaRepository<Cartridge, UUID> {
    
    Optional<Cartridge> findBySerialNumber(String serialNumber);
    
    boolean existsBySerialNumber(String serialNumber);
    
    List<Cartridge> findByModel(String model);
    
    List<Cartridge> findByStatus(CartridgeStatus status);
    
    List<Cartridge> findByCurrentLocation(Location location);
    
    List<Cartridge> findByCurrentLocationAndStatus(Location location, CartridgeStatus status);
    
    @Query("SELECT c FROM Cartridge c WHERE c.model LIKE %:model% OR c.serialNumber LIKE %:serialNumber%")
    Page<Cartridge> findByModelOrSerialNumberContaining(@Param("model") String model, 
                                                        @Param("serialNumber") String serialNumber, 
                                                        Pageable pageable);
    
    @Query("SELECT COUNT(c) FROM Cartridge c WHERE c.status = :status")
    long countByStatus(@Param("status") CartridgeStatus status);
    
    @Query("SELECT COUNT(c) FROM Cartridge c WHERE c.currentLocation = :location AND c.status = :status")
    long countByLocationAndStatus(@Param("location") Location location, 
                                 @Param("status") CartridgeStatus status);
    
    @Query("SELECT COUNT(c) FROM Cartridge c WHERE c.currentLocation = :location AND c.status = :status")
    long countByCurrentLocationAndStatus(@Param("location") Location location, 
                                        @Param("status") CartridgeStatus status);
    
    @Query("SELECT COUNT(c) FROM Cartridge c WHERE c.currentLocation = :location")
    long countByCurrentLocation(@Param("location") Location location);
} 