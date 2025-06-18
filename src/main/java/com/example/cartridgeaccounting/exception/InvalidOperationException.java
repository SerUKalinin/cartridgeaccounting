package com.example.cartridgeaccounting.exception;

public class InvalidOperationException extends RuntimeException {
    
    public InvalidOperationException(String message) {
        super(message);
    }
    
    public InvalidOperationException(String operation, String reason) {
        super("Операция '" + operation + "' невозможна: " + reason);
    }
} 