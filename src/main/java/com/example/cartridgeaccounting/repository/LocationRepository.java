package com.example.cartridgeaccounting.repository;

import com.example.cartridgeaccounting.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LocationRepository extends JpaRepository<Location, UUID> {
    
    Optional<Location> findByName(String name);
    
    List<Location> findByActive(boolean active);
    
    List<Location> findByAddressContainingIgnoreCase(String address);
    
    List<Location> findByContactPersonContainingIgnoreCase(String contactPerson);
} 