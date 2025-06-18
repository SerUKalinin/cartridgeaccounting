package com.example.cartridgeaccounting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Главный класс приложения "Система учёта картриджей МФУ".
 * Точка входа в Spring Boot приложение.
 * 
 * <p>Приложение предоставляет REST API для автоматизации учёта картриджей МФУ,
 * включая управление поступлением, выдачей, возвратом, заправкой и списанием картриджей.</p>
 * 
 * <p>Основные возможности:</p>
 * <ul>
 *   <li>Управление картриджами (создание, обновление, удаление, поиск)</li>
 *   <li>Управление объектами/местами хранения</li>
 *   <li>Управление пользователями и ролями</li>
 *   <li>Учёт операций с картриджами</li>
 *   <li>Безопасность и авторизация</li>
 *   <li>Документация API через Swagger</li>
 * </ul>
 * 
 * @author Система учёта картриджей
 * @version 1.0
 * @since 2024
 */
@SpringBootApplication
public class CartridgeAccountingApplication {
    
    /**
     * Главный метод для запуска приложения
     * 
     * @param args аргументы командной строки
     */
    public static void main(String[] args) {
        SpringApplication.run(CartridgeAccountingApplication.class, args);
    }
} 