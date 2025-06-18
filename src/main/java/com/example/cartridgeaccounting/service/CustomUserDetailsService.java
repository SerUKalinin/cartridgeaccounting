package com.example.cartridgeaccounting.service;

import com.example.cartridgeaccounting.entity.User;
import com.example.cartridgeaccounting.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Сервис для загрузки пользователей для Spring Security.
 * Реализует UserDetailsService для интеграции с системой аутентификации.
 * 
 * @author Система учёта картриджей
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class CustomUserDetailsService implements UserDetailsService {
    
    private final UserRepository userRepository;
    
    /**
     * Загружает пользователя по имени пользователя для Spring Security.
     * Этот метод вызывается автоматически при попытке аутентификации.
     * 
     * @param username имя пользователя для поиска
     * @return UserDetails объект с данными пользователя
     * @throws UsernameNotFoundException если пользователь не найден
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Загрузка пользователя для аутентификации: {}", username);
        
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Пользователь не найден для аутентификации: {}", username);
                    return new UsernameNotFoundException("Пользователь с именем '" + username + "' не найден");
                });
        
        // Проверяем, активен ли пользователь
        if (!user.isEnabled()) {
            log.warn("Попытка входа неактивного пользователя: {}", username);
            throw new UsernameNotFoundException("Пользователь '" + username + "' деактивирован");
        }
        
        log.info("Пользователь успешно загружен для аутентификации: {} (роль: {})", 
                username, user.getRole());
        
        return user; // User уже реализует UserDetails
    }
} 