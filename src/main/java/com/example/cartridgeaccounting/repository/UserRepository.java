package com.example.cartridgeaccounting.repository;

import com.example.cartridgeaccounting.entity.User;
import com.example.cartridgeaccounting.entity.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    
    Optional<User> findByUsername(String username);
    
    boolean existsByUsername(String username);
    
    List<User> findByRole(UserRole role);
    
    List<User> findByEnabled(boolean enabled);
} 