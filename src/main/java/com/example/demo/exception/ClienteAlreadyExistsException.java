package com.example.demo.exception;

/**
 * Excepción lanzada cuando se intenta crear un cliente que ya existe
 */
public class ClienteAlreadyExistsException extends RuntimeException {
    
    public ClienteAlreadyExistsException(String message) {
        super(message);
    }
}
