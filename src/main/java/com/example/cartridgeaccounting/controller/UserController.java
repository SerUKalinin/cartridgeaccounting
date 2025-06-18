package com.example.cartridgeaccounting.controller;

import com.example.cartridgeaccounting.dto.CreateUserRequest;
import com.example.cartridgeaccounting.dto.UpdateUserRequest;
import com.example.cartridgeaccounting.dto.UserDto;
import com.example.cartridgeaccounting.entity.enums.UserRole;
import com.example.cartridgeaccounting.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * REST контроллер для управления пользователями.
 * Предоставляет API для создания, обновления, удаления и поиска пользователей.
 * 
 * @author Система учёта картриджей
 * @version 1.0
 */
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Пользователи", description = "API для управления пользователями")
public class UserController {
    
    private final UserService userService;
    
    /**
     * Создает нового пользователя
     * 
     * @param request данные для создания пользователя
     * @return созданный пользователь
     */
    @PostMapping
    @Operation(summary = "Создать пользователя", description = "Создает нового пользователя в системе")
    public ResponseEntity<UserDto> createUser(@Valid @RequestBody CreateUserRequest request) {
        log.info("Запрос на создание пользователя с именем: {}", request.getUsername());
        UserDto created = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    /**
     * Получает пользователя по ID
     * 
     * @param id идентификатор пользователя
     * @return пользователь
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Получить пользователя по ID", description = "Возвращает пользователя по его уникальному идентификатору")
    public ResponseEntity<UserDto> getUserById(
            @Parameter(description = "ID пользователя") @PathVariable UUID id) {
        log.info("Запрос на получение пользователя с ID: {}", id);
        UserDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }
    
    /**
     * Получает пользователя по имени пользователя
     * 
     * @param username имя пользователя
     * @return пользователь
     */
    @GetMapping("/username/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Получить пользователя по имени", description = "Возвращает пользователя по его имени пользователя")
    public ResponseEntity<UserDto> getUserByUsername(
            @Parameter(description = "Имя пользователя") @PathVariable String username) {
        log.info("Запрос на получение пользователя по имени: {}", username);
        UserDto user = userService.getUserByUsername(username);
        return ResponseEntity.ok(user);
    }
    
    /**
     * Получает всех пользователей с пагинацией
     * 
     * @param pageable параметры пагинации
     * @return страница пользователей
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Получить всех пользователей", description = "Возвращает список всех пользователей с пагинацией")
    public ResponseEntity<Page<UserDto>> getAllUsers(Pageable pageable) {
        log.info("Запрос на получение всех пользователей с пагинацией");
        Page<UserDto> users = userService.getAllUsers(pageable);
        return ResponseEntity.ok(users);
    }
    
    /**
     * Получает пользователей по роли
     * 
     * @param role роль пользователя
     * @return список пользователей
     */
    @GetMapping("/role/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Получить пользователей по роли", description = "Возвращает список пользователей с указанной ролью")
    public ResponseEntity<List<UserDto>> getUsersByRole(
            @Parameter(description = "Роль пользователя") @PathVariable UserRole role) {
        log.info("Запрос на получение пользователей по роли: {}", role);
        List<UserDto> users = userService.getUsersByRole(role);
        return ResponseEntity.ok(users);
    }
    
    /**
     * Получает пользователей по статусу активности
     * 
     * @param enabled статус активности
     * @return список пользователей
     */
    @GetMapping("/enabled/{enabled}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Получить пользователей по статусу", description = "Возвращает список пользователей с указанным статусом активности")
    public ResponseEntity<List<UserDto>> getUsersByEnabled(
            @Parameter(description = "Статус активности") @PathVariable boolean enabled) {
        log.info("Запрос на получение пользователей по статусу активности: {}", enabled);
        List<UserDto> users = userService.getUsersByEnabled(enabled);
        return ResponseEntity.ok(users);
    }
    
    /**
     * Обновляет пользователя
     * 
     * @param id идентификатор пользователя
     * @param request данные для обновления
     * @return обновленный пользователь
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Обновить пользователя", description = "Обновляет существующего пользователя")
    public ResponseEntity<UserDto> updateUser(
            @Parameter(description = "ID пользователя") @PathVariable UUID id,
            @Valid @RequestBody UpdateUserRequest request) {
        log.info("Запрос на обновление пользователя с ID: {}", id);
        UserDto updated = userService.updateUser(id, request);
        return ResponseEntity.ok(updated);
    }
    
    /**
     * Удаляет пользователя
     * 
     * @param id идентификатор пользователя
     * @return ответ без содержимого
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Удалить пользователя", description = "Удаляет пользователя из системы")
    public ResponseEntity<Void> deleteUser(
            @Parameter(description = "ID пользователя") @PathVariable UUID id) {
        log.info("Запрос на удаление пользователя с ID: {}", id);
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
    
    /**
     * Изменяет статус пользователя
     * 
     * @param id идентификатор пользователя
     * @param enabled новый статус активности
     * @return ответ без содержимого
     */
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Изменить статус пользователя", description = "Активирует или деактивирует пользователя")
    public ResponseEntity<Void> changeUserStatus(
            @Parameter(description = "ID пользователя") @PathVariable UUID id,
            @Parameter(description = "Новый статус активности") @RequestParam boolean enabled) {
        log.info("Запрос на изменение статуса пользователя с ID: {} на активный: {}", id, enabled);
        userService.changeUserStatus(id, enabled);
        return ResponseEntity.ok().build();
    }
    
    /**
     * Изменяет пароль пользователя
     * 
     * @param id идентификатор пользователя
     * @param newPassword новый пароль
     * @return ответ без содержимого
     */
    @PatchMapping("/{id}/password")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Изменить пароль пользователя", description = "Изменяет пароль указанного пользователя")
    public ResponseEntity<Void> changeUserPassword(
            @Parameter(description = "ID пользователя") @PathVariable UUID id,
            @Parameter(description = "Новый пароль") @RequestParam String newPassword) {
        log.info("Запрос на изменение пароля для пользователя с ID: {}", id);
        userService.changeUserPassword(id, newPassword);
        return ResponseEntity.ok().build();
    }
    
    /**
     * Проверяет существование пользователя по имени
     * 
     * @param username имя пользователя
     * @return true если пользователь существует
     */
    @GetMapping("/exists/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Проверить существование пользователя", description = "Проверяет, существует ли пользователь с указанным именем")
    public ResponseEntity<Boolean> existsByUsername(
            @Parameter(description = "Имя пользователя") @PathVariable String username) {
        log.info("Запрос на проверку существования пользователя с именем: {}", username);
        boolean exists = userService.existsByUsername(username);
        return ResponseEntity.ok(exists);
    }
} 