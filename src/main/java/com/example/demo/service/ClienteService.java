package com.example.demo.service;

import com.example.demo.model.dto.ClienteRequestDTO;
import com.example.demo.model.dto.ClienteResponseDTO;

import java.util.List;

/**
 * Interfaz del servicio de clientes
 */
public interface ClienteService {
    
    /**
     * Crea un nuevo cliente
     * 
     * @param requestDTO datos del cliente a crear
     * @return cliente creado
     */
    ClienteResponseDTO crear(ClienteRequestDTO requestDTO);
    
    /**
     * Obtiene un cliente por su ID
     * 
     * @param id ID del cliente
     * @return cliente encontrado
     */
    ClienteResponseDTO obtenerPorId(Long id);
    
    /**
     * Obtiene un cliente por su nombre
     * 
     * @param nombre nombre del cliente
     * @return cliente encontrado
     */
    ClienteResponseDTO obtenerPorNombre(String nombre);
    
    /**
     * Lista todos los clientes activos
     * 
     * @return lista de clientes
     */
    List<ClienteResponseDTO> listar();
    
    /**
     * Actualiza un cliente existente
     * 
     * @param id ID del cliente a actualizar
     * @param requestDTO nuevos datos del cliente
     * @return cliente actualizado
     */
    ClienteResponseDTO actualizar(Long id, ClienteRequestDTO requestDTO);
    
    /**
     * Elimina (desactiva) un cliente
     * 
     * @param id ID del cliente a eliminar
     */
    void eliminar(Long id);
    
    /**
     * Elimina permanentemente un cliente de la base de datos
     * 
     * @param id ID del cliente a eliminar
     */
    void eliminarPermanente(Long id);
}
