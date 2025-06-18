package com.example.cartridgeaccounting.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * Глобальный обработчик исключений для REST API.
 * Обрабатывает все исключения, возникающие в контроллерах, и возвращает соответствующие HTTP ответы.
 * 
 * @author Система учёта картриджей
 * @version 1.0
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * Обрабатывает исключения при отсутствии картриджа
     * 
     * @param ex исключение отсутствия картриджа
     * @return ответ с ошибкой 404
     */
    @ExceptionHandler(CartridgeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCartridgeNotFoundException(CartridgeNotFoundException ex) {
        log.warn("Картридж не найден: {}", ex.getMessage());
        
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Картридж не найден",
                ex.getMessage()
        );
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * Обрабатывает исключения при отсутствии пользователя
     * 
     * @param ex исключение отсутствия пользователя
     * @return ответ с ошибкой 404
     */
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(UserNotFoundException ex) {
        log.warn("Пользователь не найден: {}", ex.getMessage());
        
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Пользователь не найден",
                ex.getMessage()
        );
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * Обрабатывает исключения при отсутствии объекта
     * 
     * @param ex исключение отсутствия объекта
     * @return ответ с ошибкой 404
     */
    @ExceptionHandler(LocationNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleLocationNotFoundException(LocationNotFoundException ex) {
        log.warn("Объект не найден: {}", ex.getMessage());
        
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Объект не найден",
                ex.getMessage()
        );
        
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    /**
     * Обрабатывает исключения при дублировании серийного номера картриджа
     * 
     * @param ex исключение дублирования серийного номера
     * @return ответ с ошибкой 409
     */
    @ExceptionHandler(DuplicateSerialNumberException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateSerialNumberException(DuplicateSerialNumberException ex) {
        log.warn("Дублирование серийного номера картриджа: {}", ex.getMessage());
        
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "Дублирование серийного номера",
                ex.getMessage()
        );
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    /**
     * Обрабатывает исключения при дублировании имени пользователя
     * 
     * @param ex исключение дублирования имени пользователя
     * @return ответ с ошибкой 409
     */
    @ExceptionHandler(DuplicateUsernameException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateUsernameException(DuplicateUsernameException ex) {
        log.warn("Дублирование имени пользователя: {}", ex.getMessage());
        
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.CONFLICT.value(),
                "Дублирование имени пользователя",
                ex.getMessage()
        );
        
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }

    /**
     * Обрабатывает исключения доступа запрещен
     * 
     * @param ex исключение доступа запрещен
     * @return ответ с ошибкой 403
     */
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        log.warn("Доступ запрещен: {}", ex.getMessage());
        
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.FORBIDDEN.value(),
                "Доступ запрещен",
                "У вас нет прав для выполнения этой операции"
        );
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    /**
     * Обрабатывает исключения валидации данных
     * 
     * @param ex исключение валидации
     * @return ответ с ошибками валидации
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        log.warn("Ошибка валидации данных: {}", ex.getMessage());
        
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Ошибка валидации данных",
                errors.toString()
        );
        
        return ResponseEntity.badRequest().body(errorResponse);
    }

    /**
     * Обрабатывает все остальные исключения
     * 
     * @param ex общее исключение
     * @return ответ с ошибкой 500
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        log.error("Неожиданная ошибка: {}", ex.getMessage(), ex);
        
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Внутренняя ошибка сервера",
                "Произошла неожиданная ошибка. Попробуйте позже."
        );
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    /**
     * Класс для представления ошибки в ответе API
     */
    public static class ErrorResponse {
        private LocalDateTime timestamp;
        private int status;
        private String error;
        private String message;
        
        /**
         * Конструктор для создания ответа с ошибкой
         * 
         * @param timestamp время возникновения ошибки
         * @param status HTTP статус код
         * @param error тип ошибки
         * @param message сообщение об ошибке
         */
        public ErrorResponse(LocalDateTime timestamp, int status, String error, String message) {
            this.timestamp = timestamp;
            this.status = status;
            this.error = error;
            this.message = message;
        }
        
        // Геттеры и сеттеры
        public LocalDateTime getTimestamp() {
            return timestamp;
        }
        
        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }
        
        public int getStatus() {
            return status;
        }
        
        public void setStatus(int status) {
            this.status = status;
        }
        
        public String getError() {
            return error;
        }
        
        public void setError(String error) {
            this.error = error;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
    }
} 