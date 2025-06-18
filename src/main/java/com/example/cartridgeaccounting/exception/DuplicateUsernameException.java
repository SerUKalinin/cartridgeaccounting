package com.example.cartridgeaccounting.exception;

/**
 * Исключение, возникающее при попытке создать пользователя с уже существующим именем пользователя.
 * Выбрасывается когда в системе уже есть пользователь с указанным именем пользователя.
 * 
 * @author Система учёта картриджей
 * @version 1.0
 */
public class DuplicateUsernameException extends RuntimeException {
    
    /**
     * Конструктор с именем пользователя
     * 
     * @param username имя пользователя
     */
    public DuplicateUsernameException(String username) {
        super("Пользователь с именем " + username + " уже существует");
    }
    
    /**
     * Конструктор с именем пользователя и дополнительным сообщением
     * 
     * @param username имя пользователя
     * @param message дополнительное сообщение
     */
    public DuplicateUsernameException(String username, String message) {
        super("Пользователь с именем " + username + " уже существует: " + message);
    }
} 