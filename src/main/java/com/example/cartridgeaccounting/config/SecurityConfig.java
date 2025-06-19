package com.example.cartridgeaccounting.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

/**
 * Конфигурация безопасности Spring Security.
 * Настраивает аутентификацию, авторизацию и защиту API.
 * 
 * @author Система учёта картриджей
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .cors(cors -> cors.configure(http)) // Включаем CORS
            .authorizeHttpRequests(authz -> authz
                // Разрешаем доступ к Swagger UI и API документации
                .requestMatchers("/swagger-ui/**", "/swagger-ui.html").permitAll()
                .requestMatchers("/api-docs/**").permitAll()
                .requestMatchers("/v3/api-docs/**").permitAll()
                // Разрешаем доступ к эндпоинту состояния здоровья
                .requestMatchers("/actuator/health").permitAll()
                // Разрешаем регистрацию и логин без авторизации
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/users").permitAll()
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/auth/login").permitAll()
                .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/auth/register").permitAll()
                // Все остальные запросы требуют JWT
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
} 