package com.example.cartridgeaccounting.service;

import com.example.cartridgeaccounting.dto.CreateUserRequest;
import com.example.cartridgeaccounting.dto.UpdateUserRequest;
import com.example.cartridgeaccounting.dto.UserDto;
import com.example.cartridgeaccounting.entity.enums.UserRole;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

/**
 * Сервис для работы с пользователями.
 * Предоставляет методы для создания, обновления, удаления и поиска пользователей.
 * 
 * @author Система учёта картриджей
 * @version 1.0
 */
public interface UserService {
    
    /**
     * Создает нового пользователя
     * 
     * @param request данные для создания пользователя
     * @return созданный пользователь
     */
    UserDto createUser(CreateUserRequest request);
    
    /**
     * Получает пользователя по ID
     * 
     * @param id идентификатор пользователя
     * @return пользователь
     */
    UserDto getUserById(UUID id);
    
    /**
     * Получает пользователя по имени пользователя
     * 
     * @param username имя пользователя
     * @return пользователь
     */
    UserDto getUserByUsername(String username);
    
    /**
     * Получает всех пользователей с пагинацией
     * 
     * @param pageable параметры пагинации
     * @return страница пользователей
     */
    Page<UserDto> getAllUsers(Pageable pageable);
    
    /**
     * Получает пользователей по роли
     * 
     * @param role роль пользователя
     * @return список пользователей
     */
    List<UserDto> getUsersByRole(UserRole role);
    
    /**
     * Получает пользователей по статусу активности
     * 
     * @param enabled статус активности
     * @return список пользователей
     */
    List<UserDto> getUsersByEnabled(boolean enabled);
    
    /**
     * Обновляет пользователя
     * 
     * @param id идентификатор пользователя
     * @param request данные для обновления
     * @return обновленный пользователь
     */
    UserDto updateUser(UUID id, UpdateUserRequest request);
    
    /**
     * Удаляет пользователя
     * 
     * @param id идентификатор пользователя
     */
    void deleteUser(UUID id);
    
    /**
     * Изменяет статус пользователя
     * 
     * @param id идентификатор пользователя
     * @param enabled новый статус активности
     */
    void changeUserStatus(UUID id, boolean enabled);
    
    /**
     * Изменяет пароль пользователя
     * 
     * @param id идентификатор пользователя
     * @param newPassword новый пароль
     */
    void changeUserPassword(UUID id, String newPassword);
    
    /**
     * Проверяет существование пользователя по имени
     * 
     * @param username имя пользователя
     * @return true если пользователь существует
     */
    boolean existsByUsername(String username);
} 