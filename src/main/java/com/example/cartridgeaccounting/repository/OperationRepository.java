package com.example.cartridgeaccounting.repository;

import com.example.cartridgeaccounting.entity.Cartridge;
import com.example.cartridgeaccounting.entity.Location;
import com.example.cartridgeaccounting.entity.Operation;
import com.example.cartridgeaccounting.entity.User;
import com.example.cartridgeaccounting.entity.enums.OperationType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface OperationRepository extends JpaRepository<Operation, UUID> {
    
    List<Operation> findByCartridge(Cartridge cartridge);
    
    List<Operation> findByLocation(Location location);
    
    List<Operation> findByPerformedBy(User user);
    
    List<Operation> findByType(OperationType type);
    
    List<Operation> findByOperationDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    
    @Query("SELECT o FROM Operation o WHERE o.cartridge = :cartridge ORDER BY o.operationDate DESC")
    Page<Operation> findByCartridgeOrderByDateDesc(@Param("cartridge") Cartridge cartridge, Pageable pageable);
    
    @Query("SELECT o FROM Operation o WHERE o.location = :location ORDER BY o.operationDate DESC")
    Page<Operation> findByLocationOrderByDateDesc(@Param("location") Location location, Pageable pageable);
    
    @Query("SELECT o FROM Operation o WHERE o.operationDate BETWEEN :startDate AND :endDate ORDER BY o.operationDate DESC")
    Page<Operation> findByDateRangeOrderByDateDesc(@Param("startDate") LocalDateTime startDate, 
                                                  @Param("endDate") LocalDateTime endDate, 
                                                  Pageable pageable);
    
    @Query("SELECT COUNT(o) FROM Operation o WHERE o.type = :type AND o.operationDate BETWEEN :startDate AND :endDate")
    long countByTypeAndDateRange(@Param("type") OperationType type, 
                                @Param("startDate") LocalDateTime startDate, 
                                @Param("endDate") LocalDateTime endDate);
} 