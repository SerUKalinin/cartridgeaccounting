package com.example.cartridgeaccounting.service.impl;

import com.example.cartridgeaccounting.dto.CreateUserRequest;
import com.example.cartridgeaccounting.dto.UpdateUserRequest;
import com.example.cartridgeaccounting.dto.UserDto;
import com.example.cartridgeaccounting.entity.User;
import com.example.cartridgeaccounting.entity.enums.UserRole;
import com.example.cartridgeaccounting.exception.DuplicateUsernameException;
import com.example.cartridgeaccounting.exception.UserNotFoundException;
import com.example.cartridgeaccounting.repository.UserRepository;
import com.example.cartridgeaccounting.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Реализация сервиса для работы с пользователями.
 * Предоставляет методы для создания, обновления, удаления и поиска пользователей.
 * 
 * @author Система учёта картриджей
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    /**
     * {@inheritDoc}
     */
    @Override
    public UserDto createUser(CreateUserRequest request) {
        log.info("Создание пользователя с именем: {}", request.getUsername());
        
        // Проверка на дублирование имени пользователя
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateUsernameException(request.getUsername());
        }
        
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setRole(request.getRole());
        user.setEnabled(request.isEnabled());
        
        User savedUser = userRepository.save(user);
        log.info("Пользователь создан с ID: {}", savedUser.getId());
        
        return convertToDto(savedUser);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public UserDto getUserById(UUID id) {
        log.info("Получение пользователя по ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        return convertToDto(user);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public UserDto getUserByUsername(String username) {
        log.info("Получение пользователя по имени: {}", username);
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        return convertToDto(user);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Page<UserDto> getAllUsers(Pageable pageable) {
        log.info("Получение всех пользователей с пагинацией");
        return userRepository.findAll(pageable).map(this::convertToDto);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsersByRole(UserRole role) {
        log.info("Получение пользователей по роли: {}", role);
        return userRepository.findByRole(role).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<UserDto> getUsersByEnabled(boolean enabled) {
        log.info("Получение пользователей по статусу активности: {}", enabled);
        return userRepository.findByEnabled(enabled).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public UserDto updateUser(UUID id, UpdateUserRequest request) {
        log.info("Обновление пользователя с ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        
        user.setFullName(request.getFullName());
        user.setRole(request.getRole());
        user.setEnabled(request.isEnabled());
        
        // Обновляем пароль только если он предоставлен
        if (request.getPassword() != null && !request.getPassword().trim().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        
        User updatedUser = userRepository.save(user);
        log.info("Пользователь обновлен с ID: {}", updatedUser.getId());
        
        return convertToDto(updatedUser);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void deleteUser(UUID id) {
        log.info("Удаление пользователя с ID: {}", id);
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException(id);
        }
        userRepository.deleteById(id);
        log.info("Пользователь удален с ID: {}", id);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void changeUserStatus(UUID id, boolean enabled) {
        log.info("Изменение статуса пользователя с ID: {} на активный: {}", id, enabled);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        
        user.setEnabled(enabled);
        userRepository.save(user);
        log.info("Статус пользователя изменен для ID: {}", id);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void changeUserPassword(UUID id, String newPassword) {
        log.info("Изменение пароля для пользователя с ID: {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        log.info("Пароль изменен для пользователя с ID: {}", id);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }
    
    /**
     * Преобразует сущность пользователя в DTO
     * 
     * @param user сущность пользователя
     * @return DTO пользователя
     */
    private UserDto convertToDto(User user) {
        UserDto dto = new UserDto();
        dto.setId(user.getId());
        dto.setUsername(user.getUsername());
        dto.setFullName(user.getFullName());
        dto.setRole(user.getRole());
        dto.setEnabled(user.isEnabled());
        return dto;
    }
} 