package com.example.cartridgeaccounting.exception;

public class UnauthorizedOperationException extends RuntimeException {
    
    public UnauthorizedOperationException(String message) {
        super(message);
    }
    
    public UnauthorizedOperationException(String operation, String userRole) {
        super("Операция '" + operation + "' недоступна для роли '" + userRole + "'");
    }
} 