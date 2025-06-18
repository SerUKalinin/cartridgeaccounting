package com.example.cartridgeaccounting.entity;

import com.example.cartridgeaccounting.entity.enums.UserRole;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * Сущность пользователя системы.
 * Реализует интерфейс UserDetails для интеграции со Spring Security.
 * 
 * @author Система учёта картриджей
 * @version 1.0
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    
    /**
     * Уникальный идентификатор пользователя
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    
    /**
     * Уникальное имя пользователя для входа в систему
     */
    @Column(unique = true, nullable = false)
    private String username;
    
    /**
     * Зашифрованный пароль пользователя
     */
    @Column(nullable = false)
    private String password;
    
    /**
     * Полное имя пользователя
     */
    @Column(nullable = false)
    private String fullName;
    
    /**
     * Роль пользователя в системе
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;
    
    /**
     * Статус активности пользователя
     */
    @Column(nullable = false)
    private boolean enabled = true;

    /**
     * Возвращает список прав доступа пользователя на основе его роли
     * 
     * @return коллекция прав доступа
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    /**
     * Проверяет, не истек ли срок действия аккаунта
     * 
     * @return true - аккаунт не истек
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Проверяет, не заблокирован ли аккаунт
     * 
     * @return true - аккаунт не заблокирован
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Проверяет, не истек ли срок действия учетных данных
     * 
     * @return true - учетные данные не истекли
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
} 