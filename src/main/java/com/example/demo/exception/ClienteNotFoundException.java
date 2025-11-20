package com.example.demo.exception;

/**
 * Excepción lanzada cuando un cliente no es encontrado
 */
public class ClienteNotFoundException extends RuntimeException {
    
    public ClienteNotFoundException(String message) {
        super(message);
    }
    
    public ClienteNotFoundException(Long id) {
        super("Cliente no encontrado con id: " + id);
    }
    
    public ClienteNotFoundException(String field, String value) {
        super(String.format("Cliente no encontrado con %s: %s", field, value));
    }
}
